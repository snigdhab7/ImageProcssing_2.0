# Image Processing Application - USEME

## Supported Commands

1. **load image-path image-name:**
    - Load an image from the specified path and refer to it henceforth in the program by the given image name.
    - Prerequisites/Conditions:
      - Ensure the specified image path is correct.
      - Image formats supported: jpg, png, and PPM.
2. **save image-path image-name:**
    - Save the image with the given name to the specified path which should include the name of the file.
    - Prerequisites/Conditions:
      - The specified image should exist in the program. 
      - Ensure the specified image path is correct.
3. **red-component image-name dest-image-name:**
    - Create an image with the red-component of the image with the given name, and refer to it henceforth in the program by the given destination name. 
    - Prerequisites/Conditions:
      - The specified image should exist in the program.
4. **green-component image-name dest-image-name:**
    - Similar to red-component, create an image with the green-component. 
    - Prerequisites/Conditions:
      - The specified image should exist in the program.
5. **blue-component image-name dest-image-name:**
    - Similar to red-component, create an image with the blue-component.
    - Prerequisites/Conditions:
      - The specified image should exist in the program.
6. **value-component image-name dest-image-name:**
    - Create a greyscale image representing the value component. 
    - Prerequisites/Conditions:
      - The specified image should exist in the program.
7. **luma-component image-name dest-image-name:**
    - Create a greyscale image representing the luma component.
    - Prerequisites/Conditions:
      - The specified image should exist in the program.

8. **intensity-component image-name dest-image-name:**
    - Create a greyscale image representing the intensity component.
    - Prerequisites/Conditions:
      - The specified image should exist in the program.
9. **horizontal-flip image-name dest-image-name:**
    - Flip an image horizontally.
    - Prerequisites/Conditions:
      - The specified image should exist in the program.
10. **vertical-flip image-name dest-image-name:**
    - Flip an image vertically.
    - Prerequisites/Conditions:
        - The specified image should exist in the program.

11. **brighten increment image-name dest-image-name:**
    - Brighten or darken the image by the given increment.
    - Prerequisites/Conditions:
        - The specified image should exist in the program.

12. **rgb-split image-name dest-image-name-red dest-image-name-green dest-image-name-blue:**
    - Split the given image into three images containing its red, green, and blue components respectively.
    - Prerequisites/Conditions:
        - The specified image should exist in the program.

13. **rgb-combine image-name red-image green-image blue-image:**
    - Combine three images into a single image with red, green, and blue components from the three images respectively.
    - Prerequisites/Conditions:
        - The specified image should exist in the program.

14. **blur image-name dest-image-name:**
    - Blur the given image.
    - Prerequisites/Conditions:
        - The specified image should exist in the program.

15. **sharpen image-name dest-image-name:**
    - Sharpen the given image.
    - Prerequisites/Conditions:
        - The specified image should exist in the program.

16. **sepia image-name dest-image-name:**
    - Produce a sepia-toned version of the given image.
    - Prerequisites/Conditions:
        - The specified image should exist in the program.

17. **greyscale image-name dest-image-name:**
    - Convert the image into greyscale.
    - Prerequisites/Conditions:
        - The specified image should exist in the program.

18. **run script-file:**
    - Load and run the script commands in the specified file.
    - Prerequisites/Conditions:
        - The specified image should exist in the program.

19. **compress percentage image-name dest-image-name:**
    - Create a compressed version of an image.
    - Prerequisites/Conditions:
        - The specified image should exist in the program.

20. **histogram image-name dest-image-name:**
    - Produce an image that represents the histogram of a given image.
    - Prerequisites/Conditions:
        - The specified image should exist in the program.

21. **color-correct image-name dest-image-name:**
    - Color-correct an image by aligning the meaningful peaks of its histogram.
    - Prerequisites/Conditions:
        - The specified image should exist in the program.

22. **levels-adjust b m w image-name dest-image-name:**
    - Adjust levels of an image.
    - Prerequisites/Conditions:
        - The specified image should exist in the program.

23. **< operation > image-name dest-image-name split p:**
    - Support for a split view of operations with an optional parameter for the placement of the splitting line.
    - Prerequisites/Conditions:
        - The specified image should exist in the program.

24. **-file name-of-script.txt:**
    - Accept a script file as a command-line option.
    - Prerequisites/Conditions:
        - The specified script file should exist.

25. **-text:**
    - Accept commands from terminal.
    - Prerequisites/Conditions:
        - User should input correct command syntax on terminal.


## Examples

### Load and Save Image
```bash
load panda.jpg panda
save panda-copy.jpg panda-copy
horizontal-flip panda panda-horizontal
vertical-flip panda panda-vertical
red-component panda panda-red
green-component panda panda-green
blue-component panda panda-blue
greyscale panda panda-gs
sepia panda panda-sepia
compress 20 panda panda-compressed
histogram panda panda-histogram
color-correct panda panda-color-corrected
levels-adjust 20 100 255 panda panda-levels-adjusted
run script.txt
blur panda panda-blur split 50
```

## Command-Line Arguments:

---

- `-file path-of-script-file` : Opens the script file, executes it, and then shuts down.
- `-text` : Opens in an interactive text mode, allowing the user to type the script and execute it one line at a time.
- No arguments: Opens the graphical user interface. This is what will happen if you simply double-click on the JAR file.

### Command-Line Execution of JAR file
1. **To run script file:**
```bash
java -jar ImageProcessor.jar -file script.txt
```

2. **To run interactive text mode:**
```bash
java -jar ImageProcessor.jar -text
```

3. **To run graphical user interface:**
```bash
java -jar ImageProcessor.jar
```

- Make sure you are in the `res` folder while executing the JAR file. This is to ensure that all the resources are available in the same folder for the program execution.


## GUI User Guide

---

### 1. Getting Started

#### Launching the Application
- **Method 1:** Double-click on the JAR file.
- **Method 2:** Run `java -jar ImageProcessor.jar` in the command line.

#### GUI Overview
Upon launching, you'll find a two-panel interface:
- **Left Panel:** Displays the currently loaded image.
- **Right Panel:** Shows a histogram for the loaded image and provides options for image processing operations.



### 2. Loading and Previewing Images

#### Open Image
1. Click on the "Open Image" button to launch a file chooser.
2. Choose the image file you want to process.
3. The selected image will be displayed in the left panel.
4. On the right panel, a histogram for the loaded image and a combo box for processing operations will appear.



### 3. Processing Image Operations

#### Process Image Operation
1. Select a processing operation from the combo box (e.g., "SEPIA," "BLUR," "GRAYSCALE," "SHARPEN").
2. Click on the "Process Image" button.
3. The processed image will be displayed in the left panel.
4. Choose different operations and repeat the process.


### 4. Image Preview Features

#### Preview Split Operations
For specific operations like "SEPIA," "BLUR," "GRAYSCALE," "SHARPEN," you can preview changes using the split feature.

1. Select one of the mentioned operations from the combo box.
2. Specify the split percentage in the input box.
3. Click on the "Process Image" button to see a split view of the changes.


### 5. Saving Processed Images

#### Save Processed Image
1. After processing an image, click on the "Save Image" button.
2. A directory chooser will open.
3. Choose the destination path and provide a name for the processed image.
4. Click "Save."

## GUI Operations Overview

---

### 1. Open Image
- Click on the "Open Image" button.
- Choose an image file using the file chooser.
- The selected image will appear on the left panel.

### 2. Red Component
- Select "Red Component" from the combo box.
- Click "Process Image" to view the result on the left panel.

### 3. Green Component
- Choose "Green Component" from the combo box.
- Click "Process Image" to observe the green channel effect.

### 4. Blue Component
- Opt for "Blue Component" in the combo box.
- Click "Process Image" to see the impact of the blue channel.

### 5. Value Component
- Pick "Value Component" from the combo box.
- Click "Process Image" to visualize the greyscale value component.

### 6. Luma Component
- Select "Luma Component" in the combo box.
- Click "Process Image" to see the luma component in action.

### 7. Intensity Component
- Choose "Intensity Component" from the combo box.
- Click "Process Image" to display the intensity-transformed image.

### 8. Horizontal Flip
- Opt for "Horizontal Flip" from the combo box.
- Click "Process Image" to flip the image horizontally.

### 9. Vertical Flip
- Select "Vertical Flip" in the combo box.
- Click "Process Image" to execute a vertical flip.

### 10. Brightness Adjustment
- Choose "Brightness Adjustment" from the combo box.
- Specify the increment and click "Process Image" for brightness modification.

### 11. Blur
- Choose "Blur" from the combo box.
- Click "Process Image" to apply a blurring effect.

### 12. Sharpen
- Select "Sharpen" in the combo box.
- Click "Process Image" to enhance image details.

### 13. Sepia
- Pick "Sepia" from the combo box.
- Click "Process Image" to transform the image into sepia tones.

### 14. Greyscale
- Choose "Greyscale" from the combo box.
- Click "Process Image" to convert the image to greyscale.

### 15. Save Processed Image
- After processing an image, click "Save Image."
- Choose the destination path and provide a name.
- Click "Save" to save the processed image.

### Notes
- Ensure images are loaded before applying operations.
- Some commands may have specific prerequisites. 
- The sequence of operations may affect the final result.
- If you want to execute the `-file script.txt` command using console, please update the images path for the load command as `./res/imagename.jpg` to follow the relative path.