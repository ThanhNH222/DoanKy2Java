package com.example.RentCar.controller.admin;

import com.example.RentCar.entity.Car;
import com.example.RentCar.entity.CarBrand;
import com.example.RentCar.entity.CarType;
import com.example.RentCar.repository.CarBrandRepository;
import com.example.RentCar.repository.CarRepository;
import com.example.RentCar.repository.CarTypeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/cars")
public class AdminCarController {

    private final CarRepository carRepository;
    private final CarTypeRepository carTypeRepository;
    private final CarBrandRepository carBrandRepository;

    // Thư mục upload ảnh (static/img)
    private static final String UPLOAD_DIR = "src/main/resources/static/img/";

    public AdminCarController(CarRepository carRepository,
                              CarTypeRepository carTypeRepository,
                              CarBrandRepository carBrandRepository) {
        this.carRepository = carRepository;
        this.carTypeRepository = carTypeRepository;
        this.carBrandRepository = carBrandRepository;
    }

    @GetMapping
    public String listCars(Model model) {
        List<Car> cars = carRepository.findAll();
        model.addAttribute("cars", cars);
        return "admin/cars/index";
    }

    @GetMapping("/create")
    public String createCarForm(Model model) {
        model.addAttribute("car", new Car());
        model.addAttribute("types", carTypeRepository.findAll());
        model.addAttribute("brands", carBrandRepository.findAll());
        return "admin/cars/add";
    }

    @GetMapping("/{id}/edit")
    public String editCarForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Car> car = carRepository.findById(id);
        if (car.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Car not found");
            return "redirect:/admin/cars";
        }
        model.addAttribute("car", car.get());
        model.addAttribute("types", carTypeRepository.findAll());
        model.addAttribute("brands", carBrandRepository.findAll());
        return "admin/cars/edit";
    }

    // Save chung cho cả add & edit
    @PostMapping("/save")
    public String saveCar(@ModelAttribute Car car,
                          @RequestParam(value = "carTypeId", required = false) Long carTypeId,
                          @RequestParam(value = "brandId", required = false) Long brandId,
                          @RequestParam(value = "newCarType", required = false) String newCarType,
                          @RequestParam(value = "newBrand", required = false) String newBrand,
                          @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                          RedirectAttributes redirectAttributes) {

        // --- CarType ---
        if (newCarType != null && !newCarType.trim().isEmpty()) {
            CarType type = new CarType();
            type.setName(newCarType.trim());
            carTypeRepository.save(type);
            car.setCarType(type);
        } else if (carTypeId != null) {
            carTypeRepository.findById(carTypeId).ifPresent(car::setCarType);
        }

        // --- CarBrand ---
        if (newBrand != null && !newBrand.trim().isEmpty()) {
            CarBrand brand = new CarBrand();
            brand.setName(newBrand.trim());
            carBrandRepository.save(brand);
            car.setBrand(brand);
        } else if (brandId != null) {
            carBrandRepository.findById(brandId).ifPresent(car::setBrand);
        }

        // --- Upload image ---
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());

            try {
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Lưu đường dẫn kiểu /img/filename.jpg
                car.setImageUrl("/img/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("error", "Error saving image");
                return "redirect:/admin/cars";
            }
        } else {
            // Nếu edit mà không upload ảnh mới thì giữ ảnh cũ
            if (car.getId() != null) {
                Optional<Car> oldCar = carRepository.findById(car.getId());
                oldCar.ifPresent(value -> car.setImageUrl(value.getImageUrl()));
            }
        }

        // --- Save car ---
        carRepository.save(car);
        redirectAttributes.addFlashAttribute("success", "Car saved successfully!");
        return "redirect:/admin/cars";
    }

    @PostMapping("/{id}/delete")
    public String deleteCar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Car deleted successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", "Car not found");
        }
        return "redirect:/admin/cars";
    }
}
