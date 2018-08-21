
# react-native-offline-uploader

## Getting started

`$ npm install react-native-offline-uploader --save`

### Mostly automatic installation

`$ react-native link react-native-offline-uploader`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-offline-uploader` and add `RNOfflineUploader.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNOfflineUploader.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNOfflineUploaderPackage;` to the imports at the top of the file
  - Add `new RNOfflineUploaderPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-offline-uploader'
  	project(':react-native-offline-uploader').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-offline-uploader/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-offline-uploader')
  	```

#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNOfflineUploader.sln` in `node_modules/react-native-offline-uploader/windows/RNOfflineUploader.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using Offline.Uploader.RNOfflineUploader;` to the usings at the top of the file
  - Add `new RNOfflineUploaderPackage()` to the `List<IReactPackage>` returned by the `Packages` method


## Usage
```javascript
import RNOfflineUploader from 'react-native-offline-uploader';

// TODO: What to do with the module?
RNOfflineUploader;
```
  