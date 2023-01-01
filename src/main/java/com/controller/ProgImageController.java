package com.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class ProgImageController {

    // file upload folder. this constant can later to config file
    private static final String STORAGE_FOLDER = "D://ImageUploads//";

    @PostMapping("/uploadImage")
    public ResponseEntity<String> handleImageUpload(@RequestParam("image") MultipartFile image)
            throws IOException {

        // Generate a unique identifier for the image
        String imageId = UUID.randomUUID().toString();

        // Save the image to a specific location on the server
        File imageFile = new File(STORAGE_FOLDER + imageId);
        image.transferTo(imageFile);

        // Return the image identifier in the response
        return new ResponseEntity<>(imageId, HttpStatus.OK);
    }

    @GetMapping("/getImage/{imageId}")
    public void handleRetrieveImage(@PathVariable String imageId,
                                    HttpServletResponse response) throws IOException {

        // Retrieve the image from the server
        File imageFile = new File(STORAGE_FOLDER + imageId);
        BufferedImage image = ImageIO.read(imageFile);

        // Set the content type of the response to "image/png"
        response.setContentType("image/png");

        // Write the image to the response output stream
        ImageIO.write(image, "png", response.getOutputStream());
    }


    @GetMapping("/getImageWithFileType/{imageId}/{fileType}")
    public void handleRetrieveImageWithFileType(@PathVariable String imageId, @PathVariable String fileType,
                                                HttpServletResponse response) throws IOException {

        // Retrieve the image from the server
        File imageFile = new File(STORAGE_FOLDER + imageId);
        BufferedImage image = ImageIO.read(imageFile);

        // Set the content type of the response to the appropriate value
        if (fileType.equalsIgnoreCase("PNG")) {
            response.setContentType("image/png");
        } else if (fileType.equalsIgnoreCase("JPEG") ||
                fileType.equalsIgnoreCase("JPG")) {
            response.setContentType("image/jpeg");
        } else if (fileType.equalsIgnoreCase("BMP")) {
            response.setContentType("image/bmp");
        } else if (fileType.equalsIgnoreCase("GIF")) {
            response.setContentType("image/gif");
        }

        // Convert the image to the specified file type
        BufferedImage convertedImage = new BufferedImage(image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        convertedImage.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
        ImageIO.write(convertedImage, fileType, response.getOutputStream());
    }
}
