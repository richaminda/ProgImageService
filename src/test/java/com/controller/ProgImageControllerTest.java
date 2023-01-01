package com.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProgImageControllerTest {

    private static final String STORAGE_FOLDER = "D://ImageUploads//";
    private static final String TEST_IMAGE_NAME = "D://newyear.png";
    private static final String TEST_IMAGE_NAME_JPG = "D://newyear.jpg";
    private ProgImageController progImageController;

    @Before
    public void setUp() {
        progImageController = new ProgImageController();
    }

    @Test
    public void testHandleImageUpload() throws IOException {

        File imageFile = new File(TEST_IMAGE_NAME);

        MockMultipartFile image = new MockMultipartFile("image", TEST_IMAGE_NAME, "image/png",
                Files.readAllBytes(imageFile.toPath()));

        // Call the image upload API
        ResponseEntity<String> response = progImageController.handleImageUpload(image);

        // Verify that the API returned a 200 OK status code
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Verify that the API returned a unique identifier for the image
        String imageId = response.getBody();
        assertTrue(imageId.length() > 0);

        // Verify that the image was saved to the server
        File uploadedImage = new File(STORAGE_FOLDER + imageId);
        assertTrue(uploadedImage.exists());

        //delete uploaded image
        uploadedImage.delete();

    }

    @Test
    public void testHandleRetrieveImage() throws IOException {

        // Read the image from the source location
        BufferedImage image = ImageIO.read(new File(TEST_IMAGE_NAME));
        // Generate a unique identifier for the image
        String imageId = UUID.randomUUID().toString();
        // Write the image to the destination location
        File writeImage = new File(STORAGE_FOLDER + imageId);
        ImageIO.write(image, "PNG", writeImage);

        // Create a test image file
        File imageFile = new File(TEST_IMAGE_NAME);
        BufferedImage expectedImage = ImageIO.read(imageFile);

        // Call the image download API
        MockHttpServletResponse response = new MockHttpServletResponse();
        progImageController.handleRetrieveImage(imageId, response);

        // Verify that the API returned a 200 OK status code
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        //verify file type
        assertEquals("image/png", response.getContentType());

        // Verify that the API returned the correct image
        BufferedImage actualImage = ImageIO.read(new ByteArrayInputStream(response.getContentAsByteArray()));
        assertTrue(compareBufferedImage(expectedImage, actualImage));

        //delete uploaded image
        writeImage.delete();

    }

    @Test
    public void testHandleRetrieveImageWithFileTypeJPG() throws IOException {

        // Read the image from the source location
        BufferedImage image = ImageIO.read(new File(TEST_IMAGE_NAME));
        // Generate a unique identifier for the image
        String imageId = UUID.randomUUID().toString();
        // Write the image to the destination location
        File writeImage = new File(STORAGE_FOLDER + imageId);
        ImageIO.write(image, "PNG", writeImage);

        // Call the image download API
        MockHttpServletResponse response = new MockHttpServletResponse();
        progImageController.handleRetrieveImageWithFileType(imageId, "JPG", response);

        // Verify that the API returned a 200 OK status code
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        //verify file type
        assertEquals("image/jpeg", response.getContentType());

        //delete uploaded image
        writeImage.delete();

    }

    @Test
    public void testHandleRetrieveImageWithFileTypeBMP() throws IOException {

        // Read the image from the source location
        BufferedImage image = ImageIO.read(new File(TEST_IMAGE_NAME));
        // Generate a unique identifier for the image
        String imageId = UUID.randomUUID().toString();
        // Write the image to the destination location
        File writeImage = new File(STORAGE_FOLDER + imageId);
        ImageIO.write(image, "PNG", writeImage);

        // Call the image download API
        MockHttpServletResponse response = new MockHttpServletResponse();
        progImageController.handleRetrieveImageWithFileType(imageId, "BMP", response);

        // Verify that the API returned a 200 OK status code
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        //verify file type
        assertEquals("image/bmp", response.getContentType());

        //delete uploaded image
        writeImage.delete();

    }

    @Test
    public void testHandleRetrieveImageWithFileTypeGIF() throws IOException {

        // Read the image from the source location
        BufferedImage image = ImageIO.read(new File(TEST_IMAGE_NAME));
        // Generate a unique identifier for the image
        String imageId = UUID.randomUUID().toString();
        // Write the image to the destination location
        File writeImage = new File(STORAGE_FOLDER + imageId);
        ImageIO.write(image, "PNG", writeImage);

        // Call the image download API
        MockHttpServletResponse response = new MockHttpServletResponse();
        progImageController.handleRetrieveImageWithFileType(imageId, "GIF", response);

        // Verify that the API returned a 200 OK status code
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        //verify file type
        assertEquals("image/gif", response.getContentType());

        //delete uploaded image
        writeImage.delete();

    }

    @Test
    public void testHandleRetrieveImageWithFileTypePNG() throws IOException {

        // Read the image from the source location
        BufferedImage image = ImageIO.read(new File(TEST_IMAGE_NAME));
        // Generate a unique identifier for the image
        String imageId = UUID.randomUUID().toString();
        // Write the image to the destination location
        File writeImage = new File(STORAGE_FOLDER + imageId);
        ImageIO.write(image, "PNG", writeImage);

        // Call the image download API
        MockHttpServletResponse response = new MockHttpServletResponse();
        progImageController.handleRetrieveImageWithFileType(imageId, "PNG", response);

        // Verify that the API returned a 200 OK status code
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        //verify file type
        assertEquals("image/png", response.getContentType());

        //delete uploaded image
        writeImage.delete();

    }

    public boolean compareBufferedImage(BufferedImage img1, BufferedImage img2) {
        boolean imagesEqual = true;
        for (int x = 0; x < img1.getWidth(); x++) {
            for (int y = 0; y < img1.getHeight(); y++) {
                if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
                    imagesEqual = false;
                    break;
                }
            }
        }
        return imagesEqual;
    }
}
