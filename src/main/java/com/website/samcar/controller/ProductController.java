// Project structure and core files will be written here in the next steps

// Updated ProductController to support edit/delete via POST (no ID in URL)
package com.website.samcar.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.website.samcar.model.Product;
import com.website.samcar.service.ProductService;

@Controller
public class ProductController {

    private final AuthController authController;

    @Autowired
    private ProductService productService;

    ProductController(AuthController authController) {
        this.authController = authController;
    }
    
    
     
    private String saveImage(MultipartFile file, String uploadDir) throws IOException {
        if (file != null && !file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir, fileName);
            Files.write(path, file.getBytes());
            return "/images/" + fileName;
        }
        return null;
    }

    @GetMapping("/add-product")
    @PreAuthorize("hasRole('ADMIN')")
    public String showForm(Model model) {
        model.addAttribute("product", new Product());
        return "add_product";
    }
    @GetMapping("/edit-request-product")
    @PreAuthorize("hasRole('ADMIN')")
    public String editRequest(Model model) {
        model.addAttribute("product", new Product());
        return "edit_product";
    }

    @PostMapping("/products")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveProduct(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("fuel") String fuel,
            @RequestParam("body") String body,
            @RequestParam("fuelcapacity") int fuelcapacity,
            @RequestParam("transmission") String transmission,
            @RequestParam("horsepower") int horsepower,
            @RequestParam("model") String model,
            @RequestParam("seating") int seating,
            @RequestParam("price") Double price,
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("image2") MultipartFile imageFile2,
            @RequestParam("image3") MultipartFile imageFile3,
            @RequestParam("image4") MultipartFile imageFile4,
            @RequestParam("image5") MultipartFile imageFile5
    ) throws IOException {

    	String uploadDir = new File("uploads/images").getAbsolutePath();
    	File dir = new File(uploadDir);
    	if (!dir.exists()) dir.mkdirs(); // create folder if it doesn't exist

    	String imagePath1 = saveImage(imageFile, uploadDir);
    	String imagePath2 = saveImage(imageFile2, uploadDir);
    	String imagePath3 = saveImage(imageFile3, uploadDir);
    	String imagePath4 = saveImage(imageFile4, uploadDir);
    	String imagePath5 = saveImage(imageFile5, uploadDir);
    
    Product product = new Product();
    product.setName(name);
    product.setDescription(description);
    product.setFuel(fuel);
    product.setBody(body);
    product.setFuelcapacity(fuelcapacity);
    product.setTransmission(transmission);
    product.setHorsepower(horsepower);
    product.setModel(model);
    product.setSeating(seating);
    product.setPrice(price);
    product.setImageUrl(imagePath1);
    product.setImageUrl2(imagePath2);
    product.setImageUrl3(imagePath3);
    product.setImageUrl4(imagePath4);
    product.setImageUrl5(imagePath5);

    productService.saveProduct(product);
        return "redirect:/dashboard";
    }

    @GetMapping("/home")
    public String getProducts(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String role = authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .findFirst()
                        .orElse("");

        model.addAttribute("role", role); // Pass role to view

        model.addAttribute("products", productService.getAllProducts());
        return "home";
    }
    
    @GetMapping("/cars")
    public String getCars(Model model) {
    	model.addAttribute("products", productService.getAllProducts());
    	return "cars";
    }
    @GetMapping("/pricing")
    public String pricing() {
    	return "pricing";
    }
    
    @GetMapping("/customerTestimonial")
    public String customerTestimonials() {
    	return "customerTestimonial";
    }
    
    
    

    @GetMapping("/carDetails/{id}")
    public String getProductDetails(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id); // Fetch product by ID
        model.addAttribute("product", product);
        return "carDetails"; // Thymeleaf template name: carDetails.html
    }

    @GetMapping("/edit-product/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/error"; // or custom 404 page
        }
        model.addAttribute("product", product);
        return "edit_product"; // Same form as add_product but reused
    }

    @PostMapping("/update-product")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateProduct(
            @RequestParam("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("fuel") String fuel,
            @RequestParam("body") String body,
            @RequestParam("fuelcapacity") int fuelcapacity,
            @RequestParam("transmission") String transmission,
            @RequestParam("horsepower") int horsepower,
            @RequestParam("model") String modelStr,
            @RequestParam("seating") int seating,
            @RequestParam("price") Double price,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "imageFile2", required = false) MultipartFile imageFile2,
            @RequestParam(value = "imageFile3", required = false) MultipartFile imageFile3,
            @RequestParam(value = "imageFile3", required = false) MultipartFile imageFile4,
            @RequestParam(value = "imageFile5", required = false) MultipartFile imageFile5
    ) throws IOException {

        Product product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/error";
        }

        product.setName(name);
        product.setDescription(description);
        product.setFuel(fuel);
        product.setBody(body);
        product.setFuelcapacity(fuelcapacity);
        product.setTransmission(transmission);
        product.setHorsepower(horsepower);
        product.setModel(modelStr);
        product.setSeating(seating);
        product.setPrice(price);

        String uploadDir = new File("uploads/images").getAbsolutePath();
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        if (imageFile != null && !imageFile.isEmpty())
            product.setImageUrl(saveImage(imageFile, uploadDir));

        if (imageFile2 != null && !imageFile2.isEmpty())
            product.setImageUrl2(saveImage(imageFile2, uploadDir));

        if (imageFile3 != null && !imageFile3.isEmpty())
            product.setImageUrl3(saveImage(imageFile3, uploadDir));

        if (imageFile4 != null && !imageFile4.isEmpty())
            product.setImageUrl4(saveImage(imageFile4, uploadDir));

        if (imageFile5 != null && !imageFile5.isEmpty())
            product.setImageUrl5(saveImage(imageFile5, uploadDir));

        productService.saveProduct(product);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/delete-product")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteProductFromForm(@RequestParam("id") Long id) {
        productService.deleteProduct(id);
        System.out.println("deleting product with id: "+id);
        return "redirect:/dashboard";
    }
    
}
