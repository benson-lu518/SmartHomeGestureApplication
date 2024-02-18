# Smarthome
An android application developed as a part of CSE 535 (Mobile Computing &amp; ML)

## Usage:
Run the 'server.py' & 'app.py' files in the server directory. Flask will listen for requests on 0.0.0.0 (127.0.0.1:5000).
Compile the android project to APK and install on your mobile device or emulator.
The application will allow you to view and practice different gestures. 
Record three videos of each gesture. The gesture videos are then uploaded to the flask server.
These files are classified based upon their cosine similarity of their middle frames to certain images of gestures that the CNN is trained to recognize.


## Todo: 
The CNN & ML / data science part of the application still needs to be uploaded to github, it is too large to be stored. Looking into githubLFS or deploying on the cloud.
Allow home devices to connect via bluetooth and, based upon the gesture submitted, control the device.
Improve the accuracy & precision of recognition. 
