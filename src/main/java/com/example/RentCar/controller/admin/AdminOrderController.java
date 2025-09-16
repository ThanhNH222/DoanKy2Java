package com.example.RentCar.controller.admin;

import com.example.RentCar.entity.ImageType;
import com.example.RentCar.entity.Rental;
import com.example.RentCar.entity.RentalImage;
import com.example.RentCar.repository.RentalImageRepository;
import com.example.RentCar.repository.RentalRepository;
import com.example.RentCar.service.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/orders")   // prefix dành cho admin
public class AdminOrderController {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private RentalImageRepository rentalImageRepository;

    @Autowired
    private EmailService emailService;

    /** ---------------- Danh sách đơn thuê ---------------- */
    @GetMapping
    public String listOrders(@RequestParam(required = false) String keyword,
                             @RequestParam(required = false) String status,
                             Model model) {

        List<Rental> orders;

        if (keyword != null && !keyword.isEmpty() && status != null && !status.isEmpty()) {
            orders = rentalRepository
                    .findByStatusAndCustomerNameContainingIgnoreCaseOrStatusAndCustomerEmailContainingIgnoreCase(
                            status, keyword, status, keyword);
        } else if (keyword != null && !keyword.isEmpty()) {
            orders = rentalRepository
                    .findByCustomerNameContainingIgnoreCaseOrCustomerEmailContainingIgnoreCase(keyword, keyword);
        } else if (status != null && !status.isEmpty()) {
            orders = rentalRepository.findByStatus(status);
        } else {
            orders = rentalRepository.findAll();
        }

        model.addAttribute("orders", orders);
        model.addAttribute("keyword", keyword);
        model.addAttribute("status", status);
        return "admin/orders/index";
    }

    /** ---------------- Chi tiết đơn thuê ---------------- */
    @GetMapping("/detail/{id}")
    public String rentalDetail(@PathVariable Long id, Model model) {
        Rental order = rentalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy đơn thuê"));

        List<RentalImage> contractImages =
                rentalImageRepository.findByRentalIdAndImageType(id, ImageType.CONTRACT);
        List<RentalImage> handoverImages =
                rentalImageRepository.findByRentalIdAndImageType(id, ImageType.HANDOVER);
        List<RentalImage> returnImages =
                rentalImageRepository.findByRentalIdAndImageType(id, ImageType.RETURN);

        model.addAttribute("order", order);
        model.addAttribute("contractImages", contractImages);
        model.addAttribute("handoverImages", handoverImages);
        model.addAttribute("returnImages", returnImages);

        return "admin/orders/detail";
    }

    /** ---------------- Gửi email xác nhận ---------------- */
    @PostMapping("/send-confirm-email")
    public String sendConfirmEmail(@RequestParam Long orderId) throws MessagingException {
        Rental rental = rentalRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid order ID"));

        rental.setStatus("WAITING_CONFIRM");

        String token = UUID.randomUUID().toString();
        rental.setConfirmationToken(token);

        // link xác nhận dành cho khách
        String confirmUrl = "http://localhost:8080/rentals/confirm?token=" + token;

        emailService.sendStatusUpdateEmail(rental, confirmUrl);

        rentalRepository.save(rental);
        return "redirect:/admin/orders/detail/" + orderId;
    }

    /** ---------------- Upload ảnh hợp đồng ---------------- */
    @PostMapping("/upload-contract")
    public String uploadContract(@RequestParam("rentalId") Long rentalId,
                                 @RequestParam("contractImages") List<MultipartFile> contractImages,
                                 RedirectAttributes redirectAttributes) {
        saveImages(rentalId, contractImages, "contracts", ImageType.CONTRACT, redirectAttributes);
        return "redirect:/admin/orders/detail/" + rentalId;
    }

    /** ---------------- Upload ảnh giao xe ---------------- */
    @PostMapping("/upload-handover")
    public String uploadHandover(@RequestParam("rentalId") Long rentalId,
                                 @RequestParam("handoverImages") List<MultipartFile> handoverImages,
                                 RedirectAttributes redirectAttributes) {
        saveImages(rentalId, handoverImages, "handovers", ImageType.HANDOVER, redirectAttributes);
        return "redirect:/admin/orders/detail/" + rentalId;
    }

    /** ---------------- Upload ảnh nhận xe ---------------- */
    @PostMapping("/upload-return")
    public String uploadReturn(@RequestParam("rentalId") Long rentalId,
                               @RequestParam("returnImages") List<MultipartFile> returnImages,
                               RedirectAttributes redirectAttributes) {
        saveImages(rentalId, returnImages, "returns", ImageType.RETURN, redirectAttributes);
        return "redirect:/admin/orders/detail/" + rentalId;
    }
    /** ---------------- Hàm lưu ảnh dùng chung ---------------- */
    private void saveImages(Long rentalId,
                            List<MultipartFile> files,
                            String folder,
                            ImageType type,
                            RedirectAttributes redirectAttributes) {
        try {
            Path uploadPath = Paths.get("uploads/" + folder + "/");
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                    RentalImage rentalImage = new RentalImage();
                    rentalImage.setRentalId(rentalId);
                    rentalImage.setImageUrl("/uploads/" + folder + "/" + fileName);
                    rentalImage.setImageType(type);
                    rentalImageRepository.save(rentalImage);
                }
            }
            redirectAttributes.addFlashAttribute("success", "Upload ảnh " + type + " thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi upload ảnh " + type + ": " + e.getMessage());
        }
    }

    /** ---------------- Cập nhật ghi chú ---------------- */
    @PostMapping("/update-notes")
    public String updateAdminNotes(@RequestParam("rentalId") Long rentalId,
                                   @RequestParam("adminNotes") String adminNotes,
                                   RedirectAttributes redirectAttributes) {
        try {
            Rental rental = rentalRepository.findById(rentalId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn thuê xe"));

            rental.setAdminNotes(adminNotes);
            rentalRepository.save(rental);

            redirectAttributes.addFlashAttribute("success", "Cập nhật ghi chú admin thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật ghi chú admin: " + e.getMessage());
        }

        return "redirect:/admin/orders/detail/" + rentalId;
    }



}
