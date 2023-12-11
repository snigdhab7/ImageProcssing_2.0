# Image Processing Application
---

This repository hosts an image processing application developed as part of a series of assignments, aiming to implement a robust system for various image manipulations. The application is structured to include text-based and interactive GUI-based interfaces, providing users with a versatile set of tools for image enhancement and modification.
### Authors

- [Akshay Chavan](https://www.linkedin.com/in/akshaychavan7/)
- [Sanjana Poojary](https://www.linkedin.com/in/sanjanapoojary22/)

### Course Information

- **Course:** Programming Design Paradigm
- **Course Instructor:** [Prof. Amit Shesh](https://www.khoury.northeastern.edu/people/amit-shesh/)
- **Institution:** Northeastern University


### Introduction
--- 
Welcome to the Image Processing Application! Our platform is meticulously crafted to offer a comprehensive suite of tools empowering users with the ability to manipulate and enhance images in diverse and creative ways. This application is a sophisticated canvas for a multitude of transformations, providing a wide spectrum of tools to modify and elevate images.

### Purpose
Our aim is to introduce users to an innovative platform, allowing them to explore the depths of image processing. With an array of features and a user-friendly interface, users can experience a rich set of image manipulation operations, from fundamental adjustments to intricate transformations.

### Features
This application is designed with an assortment of features enabling users to perform various image operations:

Pixel-Level Modifications: Enhance and adjust individual pixels, offering flexibility in altering color components.
Image-Wide Transformations: Apply global transformations to manipulate entire images, such as flipping, brightness adjustments, and sharpening.
Advanced Filtering: Incorporate complex filtering techniques for blur effects or edge enhancements.
Color Space Transformations: Implement color space manipulations like greyscale conversion and sepia tone effects.
User-Driven Scripting: A text-based script interface to facilitate easy, command-based image manipulation, making it convenient for users to perform multiple operations sequentially.

### Classes

1. **ImageProcessingController**
    - **Purpose:** Acts as the main control center, managing and overseeing the functionalities of the application.
    - **Responsibilities:** Orchestrates image processing operations, including the interaction with various transformation classes, and manages the user interface.

2. **AbstractImageTransformation**
    - **Purpose:** Serves as an abstract blueprint for specific image transformation classes.
    - **Responsibilities:** Defines the basic structure and common functionalities required for different image transformation operations, primarily through the `filter` method.

3. **Blur**
    - **Purpose:** Implements a blurring effect on the input image using a pre-defined blur kernel matrix.
    - **Responsibilities:** Executes the blur operation, decreasing the image's sharpness and focusing.

4. **Brightness**
    - **Purpose:** Brightens the input image by modifying the RGB values of each pixel.
    - **Responsibilities:** Implements the brightening effect by adjusting the RGB values based on an input brightness change constant.

5. **GreenComponent**
    - **Purpose:** Extracts the green component of an image.
    - **Responsibilities:** Isolates and extracts the green channel from the input image, while zeroing out the other channels.

6. **Greyscale**
    - **Purpose:** Converts an image into greyscale using weighted averages of RGB values.
    - **Responsibilities:** Transforms the image into various shades of grey based on weighted averages of RGB values.

7. **HorizontalFlip**
    - **Purpose:** Performs a horizontal flip on the input image.
    - **Responsibilities:** Generates a horizontally flipped image by manipulating the pixel matrix horizontally.

8. **IntensityComponent**
    - **Purpose:** Extracts the intensity component from an image.
    - **Responsibilities:** Calculates the intensity by averaging the RGB values of each pixel.

9. **LumaComponent**
    - **Purpose:** Extracts the luma component from an image.
    - **Responsibilities:** Derives the luma component by using weighted averages of the RGB values.

10. **RedComponent**
    - **Purpose:** Isolates the red component of an image.
    - **Responsibilities:** Isolates the red channel in the image while nullifying the other channels.

11. **RgbCombine**
    - **Purpose:** Merges individual Red, Green, and Blue channel images into a single image.
    - **Responsibilities:** Combines three separate channel matrices into one complete image.

12. **Sepia**
    - **Purpose:** Transforms an image into a sepia-tone version.
    - **Responsibilities:** Applies a sepia-tone filter to the input image.

13. **Sharpen**
    - **Purpose:** Enhances an image using a predefined kernel matrix.
    - **Responsibilities:** Improves image details by applying a sharpening effect.

14. **TransformationHelper**
    - **Purpose:** Provides essential methods to apply transformations on image pixel matrices.
    - **Responsibilities:** Implements the logic for image transformation using kernel matrices.

15. **ValueComponent**
    - **Purpose:** Retains the maximum value of the RGB components of each pixel.
    - **Responsibilities:** Sets all RGB components to the maximum of the three values.

16. **VerticalFlip**
    - **Purpose:** Flips the input image vertically.
    - **Responsibilities:** Produces a vertically flipped image using matrix operations.

17. **RGBSplit**
    - **Purpose:** Splits an image into its individual red, green, and blue components.
    - **Responsibilities:** Divides the input image into three separate images, each representing the red, green, or blue component, discarding the rest of the color information.
18. **BlueComponent**
    - **Purpose:** Extracts the blue component from an image.
    - **Responsibilities:** Isolates and retrieves only the blue channel from the input image while setting the red and green channels to zero, emphasizing the blue color component.
19. **ProgramRunner**
    - **Purpose:** Serves as the entry point for the Image Processing application.
    - **Responsibilities:** Initializes the ImageProcessingController to manage image processing functionalities.

20. **InputStreamController**
    - **Purpose:** Implements a controller that takes input from an InputStream, used for text-based scripting.
    - **Responsibilities:** Processes commands provided through an InputStream and delegates them to the ImageProcessingController.

21. **ImageModel**
    - **Purpose:** Represents the underlying data model for images.
    - **Responsibilities:** Manages the data and state of images used in the application.

22. **ImageProcessingView**
    - **Purpose:** Represents the view for the GUI-based interface.
    - **Responsibilities:** Displays the image and user interface for the GUI-based application.

23. **ViewInterface**
    - **Purpose:** Defines the interface for views in the application.
    - **Responsibilities:** Specifies the methods that views must implement.

24. **GUIController**
    - **Purpose:** Implements a controller for the GUI-based interface.
    - **Responsibilities:** Manages user interactions and delegates them to the ImageProcessingController.


### Enums

1. **Methods**
    - **Purpose:** Enumerates various image transformation methods available.
    - **Responsibilities:** Lists and categorizes different image processing operations supported in the application.

2. **TransformationType**
    - **Purpose:** Enumerates types of transformations applicable to image pixel matrices.
    - **Responsibilities:** Distinguishes between filter and transformation operations.

### Interfaces

1. **ImageTransformation**
    - **Purpose:** Represents an image transformation operation.
    - **Responsibilities:** Provides a standardized method to apply transformations on images and obtain modified pixel matrices.
2. **ViewInterface**
    - **Purpose:** Defines the contract for the view components in the image processing application.
    - **Responsibilities:** Specifies methods for updating the view based on image transformations and handling user interactions.

3. **ControllerInterface**
    - **Purpose:** Declares the interface for the controllers in the image processing application.
    - **Responsibilities:** Provides methods for starting the application, setting the view, and processing image operations.

4. **ImageTransformation**
    - **Purpose:** Represents an image transformation operation.
    - **Responsibilities:** Provides a standardized method to apply transformations on images and obtain modified pixel matrices.

5. **ImageModelObserver**
    - **Purpose:** Defines the contract for classes that observe changes in the ImageModel.
    - **Responsibilities:** Specifies a method to be called when the observed ImageModel undergoes a change.

6. **ImageModelSubject**
    - **Purpose:** Declares the interface for classes that act as subjects in the observer pattern for ImageModel observers.
    - **Responsibilities:** Provides methods to register, remove, and notify observers of changes in the ImageModel.


### Summary
The assignment tasks the development of an image processing application capable of performing a myriad of operations, from basic to advanced image manipulations. It involves handling different file formats like jpg, png, and the PPM format. The system's core functionalities include:

- Loading and saving images in various formats
- Image flipping, brightness adjustments, and channel splitting
- Implementing blurring, sharpening, and applying color transformations like greyscale and sepia effects
- Supporting text-based scripting for operation execution



### Execution Steps
---
- #### Running Individual Commands

1. **Load an Image:**
   ```bash
   $ load res/panda.jpg panda

2. **Execute Operations:**
- To perform a red component operation:
    ```bash
    $ red-component panda panda-red
- To horizontally flip an image:
    ```bash
    $ horizontal-flip panda-red panda-red-horizontal
- Likewise, use other commands to perform respective operations.
3. **Save the Image:**
    ```bash
    $ save panda-red-horizontal.jpg panda-red-horizontal
- #### Running Script File

  Users can also execute a script file that contains a sequence of commands. The following command executes a script file:
  To perform a red component operation:
    ```bash
    $ run res/script.txt


## Design Changes and Justifications

---

## Design Changes

### 1. **Introduction of GUIController**

- **Change:**
    - Added a new class `GUIController` to handle GUI-based interactions.
- **Justification:**
    - The introduction of a GUI controller enhances the user experience by providing an interactive graphical interface for image processing operations. This extends the application's usability beyond command-line interactions, making it more accessible to users who prefer a visual interface.

### 2. **Introduction of ImageProcessingView**

- **Change:**
    - Added a new class `ImageProcessingView` to represent the view component in the image processing application.
- **Justification:**
    - The `ImageProcessingView` class is introduced to display processed images and provide a user interface for interactions. Separating the view component improves the application's modularity and adheres to the Model-View-Controller (MVC) design pattern.

### 3. **Introduction of Features Interface**

- **Change:**
    - Introduced a `Features` interface to abstract application-specific events and hide Swing-specific event listeners.
- **Justification:**
    - The `Features` interface provides a clean abstraction for application-specific events, decoupling them from Swing-specific details. This enhances testability and allows for easier mock view creation in testing.

### 4. **Updates to ProgramRunner**

- **Change:**
    - Updated the `ProgramRunner` class to consider command-line arguments for different application entry points.
- **Justification:**
    - The changes in `ProgramRunner` allow users to start the application in various modes, including GUI mode, text-based mode, or script execution mode. This provides users with flexibility based on their preferences and requirements.

## Justifications

### 1. **Enhanced User Interaction**

- **Justification:**
    - The introduction of `GUIController` and `ImageProcessingView` enhances user interaction by providing a visual and interactive interface. This makes the application more user-friendly and appealing to users who may not be familiar with command-line operations.

### 2. **Improved Flexibility in Input**

- **Justification:**
    - The addition of `InputStreamController` allows users to input commands through a script file or standard input. This flexibility accommodates users who prefer to script a sequence of operations or provide commands interactively.

### 3. **Modularity and Separation of Concerns**

- **Justification:**
    - The introduction of `ImageProcessingView` contributes to a more modular design by separating the view component. This adheres to the MVC design pattern, making the application more maintainable and extensible.

### 4. **Command-Line Argument Handling**

- **Justification:**
    - The updates to `ProgramRunner` provide a clear and structured way to handle different entry points. Users can choose their preferred mode of interaction by specifying command-line arguments, adding versatility to the application.

### 5. **Abstraction with Features Interface**

- **Justification:**
    - The `Features` interface abstracts application-specific events, promoting testability and ease of mock view creation. It allows for clean separation between UI events and behavior, facilitating more controlled and predictable testing scenarios.

## Conclusion

These design changes and justifications aim to improve the overall functionality, usability, and maintainability of the Image Processing Application. By introducing GUI components, enhancing input flexibility, and adhering to design patterns, the application becomes more versatile and user-friendly.

## Citations for Images
---
### 1. Koala.ppm
- **Description**: Koala
- **Source**: Provided in the assignment starter code

### 2. [panda.jpg](https://commons.wikimedia.org/wiki/File:Panda_Cub_%284274178112%29.jpg)
- **Description**: Panda Cub
- **Date**: 23 May 2009, 10:54
- **Source**: Panda Cub
- **Author**: kevinmcgill from Den Bosch, Netherlands
- **License**: [CC BY-SA 2.0](https://creativecommons.org/licenses/by-sa/2.0/)
- **Citation**: The original image can be found [here](https://creativecommons.org/licenses/by-sa/2.0/).

### 3. [dog.png](https://www.freeiconspng.com/img/22667)
- **Author**: Ahkâm
- **Website**: [Free Icons PNG](https://www.freeiconspng.com/img/22667#google_vignette)
- **License**: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/)
- **Citation**: The image source can be accessed [here](https://www.freeiconspng.com/img/22667).

---

