# ImagePicker
------------------
------------------

Image Picker allows you to select image from gallery and 
get those images in onActivityResult of your activity 



For Gradle dependency please add following lines.
Add Maven repository in your app build.gradle file

    android{
        .....
        repositories {
            maven {
                url 'https://dl.bintray.com/vikasgoyal/maven'
            }
        }

     }

Add dependency with following lines on build.gradle.

        compile 'com.avi.android:imagepicker:1.0.0'


For Maven dependency please add following lines:

    <dependency>
      <groupId>com.avi.android</groupId>
      <artifactId>imagepicker</artifactId>
      <version>1.0.0</version>
      <type>pom</type>
    </dependency>
    
You can simply launch the ImagePickerActivity for image picking like:-

<h4><b>For Single Image picking</b></h4>
 
        Intent pickerChooser = new Intent(ImagePickerActivity.ACTION_INTENT);
        pickerChooser.putExtra(ImagePickerActivity.ACTION_MODE, ImagePickerActivity.PICK_SINGLE_IMAGE);
        startActivityForResult(pickerChooser, RESILT_CODE_SINGLE_CHOICE);
        
<h4><b>For Multiple Images picking</b></h4>
 
        Intent pickerChooser = new Intent(ImagePickerActivity.ACTION_INTENT);
        pickerChooser.putExtra(ImagePickerActivity.ACTION_MODE, ImagePickerActivity.PICK_MULTIPLE_IMAGE);
        startActivityForResult(pickerChooser, RESILT_CODE_MULTI_CHOICE);
        
        
And you will get callback for array of Image Uri in onActivityResult of your current activity like this:-
     
            @Override
                public void onActivityResult(int requestCode, int resultCode, Intent data) {
                    super.onActivityResult(requestCode, resultCode, data);
                    if (data == null) {
                        return;
                    }
                    Parcelable[] parcelableUris = data.getParcelableArrayExtra(ImagePickerActivity.TAG_IMAGE_URI);
                    Uri[] uris = new Uri[parcelableUris.length];
                    System.arraycopy(parcelableUris, 0, uris, 0, parcelableUris.length);
                     if (requestCode == RESILT_CODE_MULTI_CHOICE) {
                         //Handle multiple images uri from here
                     } else if (requestCode == RESILT_CODE_SINGLE_CHOICE) {
                         //Handle single images uri from here
                     }
            }