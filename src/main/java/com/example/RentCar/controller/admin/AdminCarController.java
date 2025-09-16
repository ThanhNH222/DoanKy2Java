package com.example.RentCar.controller.admin;

import com.example.RentCar.entity.Car;
import com.example.RentCar.repository.CarRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.List;

@Controller
@RequestMapping("/admin/cars")
public class AdminCarController {

    private final CarRepository carRepository;

    public AdminCarController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @GetMapping
    public String index(@RequestParam(value="q", required=false) String q, Model model) {
        List<Car> cars = (q == null || q.isBlank())
                ? carRepository.findAll()
                : carRepository.searchCars(q);
        model.addAttribute("cars", cars);
        return "admin/cars/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("car", new Car());
        model.addAttribute("mode", "create");
        return "admin/cars/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("car") Car car,
                       BindingResult result,
                       RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "admin/cars/form";
        }
        // Ensure creating new
        try { car.setId(null); } catch (Exception ignored) {}
        carRepository.save(car);
        ra.addFlashAttribute("success", "Thêm xe thành công");
        return "redirect:/admin/cars";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<Car> car = carRepository.findById(id);
        if (car.isEmpty()) {
            ra.addFlashAttribute("error", "Không tìm thấy xe");
            return "redirect:/admin/cars";
        }
        model.addAttribute("car", car.get());
        model.addAttribute("mode", "edit");
        return "admin/cars/form";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id,
                         @ModelAttribute("car") Car form,
                         BindingResult result,
                         RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "admin/cars/form";
        }
        Optional<Car> existing = carRepository.findById(id);
        if (existing.isEmpty()) {
            ra.addFlashAttribute("error", "Không tìm thấy xe");
            return "redirect:/admin/cars";
        }
        Car car = existing.get();
        // Update a few common fields safely
        try { car.getClass().getMethod("setName", String.class).invoke(car, form.getName()); } catch (Exception ignored) {}
        try { car.getClass().getMethod("setLicensePlate", String.class).invoke(car, form.getClass().getMethod("getLicensePlate").invoke(form)); } catch (Exception ignored) {}
        try { car.getClass().getMethod("setSeatNumber", Integer.class).invoke(car, form.getClass().getMethod("getSeatNumber").invoke(form)); } catch (Exception ignored) {}

        carRepository.save(car);
        ra.addFlashAttribute("success", "Cập nhật xe thành công");
        return "redirect:/admin/cars";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
            ra.addFlashAttribute("success", "Đã xóa xe");
        } else {
            ra.addFlashAttribute("error", "Xe không tồn tại");
        }
        return "redirect:/admin/cars";
    }
}