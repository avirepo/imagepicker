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
