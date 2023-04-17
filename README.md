# PermissionsTest
A sample app which demonstrates how to request permissions in Jetpack Compose

### Accompanist permissions library
This sample app uses the [Accompanist permissions library](https://pages.github.com/). This library provides two remember functions which grant access to permission state within a composable:

- rememberPermissionState
  - Used for requesting a single permission and checking the status of that permission

- rememberMultiplePermissionsState
  - Used for requesting multiple permissions in a row and checking the status of all of them
  
### Screen blocking permissions
Screen blocking permissions are those which block access to entire screens until granted. This sample app provides a PermissionRequiredScreen composable which wraps a screen composable and requests one or more permissions before allowing access to it.

### Action blocking permissions
Action blocking permissions are those which block access to an action until granted. This sample app demonstrates action blocking permissions through button clicks that will request permission and execute an action if the permission is granted, otherwise block the action and show a rationale explaining why the permission is needed.

### Limitations
The Android permissions framework is limited in that it cannot tell the difference between the first time a permission is requested and the “deny and don’t ask again” case. In both of these cases, the permissionState will return a status of denied with shouldShowRationale = false.

Thus, it is not possible to detect that a user has permanently denied a permission, at least not without persisting flags to DataStore which complicates the process and may not be reliable if future versions of Android change permission behavior.

### Detecting permission results
This sample app makes use of the onPermissionResult lambda that we can pass to rememberPermissionState and rememberMultiplePermissionsState. This lambda will be executed after calling the state’s method to request the permission and any of the following are true:

- For a single permission request:
  - User grants or denies the permission in the permission request dialog
  - Permission is already granted
  - Permission is already permanently denied
  - User dismisses permission request dialog via back button or clicking outside of it

- For a multiple permissions request:
  - User finishes going through the flow of granting or denying permissions in all permission request dialogs
  - All permissions are already granted
  - All permissions are already permanently denied
  - User dismisses any permission request dialog via back button or clicking outside of it

The lambda will be passed a boolean which indicates whether or not the permission was granted, or in the case where multiple permissions were requested, it will be passed a map of permission to grant result. It is here where we will detect the first denied permission and modify a compose state variable to show the permission denied content, typically a dialog or screen which displays a rationale corresponding to the denied permission.

This sample app provides a configurable and reusable PermissionDialog where the rationale can be displayed. Additionally, the dialog provides an action button which will allow the permission to be requested again when shouldShowRationale is true. If shouldShowRationale is false, it is likely that the permission has been permanently denied and the action button will instead link to app settings where the user can re-enable the permission.

### Refreshing permissions
PermissionRequiredScreen observes the activity lifecycle and requests the permission during onStart. It does this to ensure that the permission status refreshes upon navigating away from the app and back. For example, a user may launch app settings to modify a permission then return to the app.

Note that this approach works great when using rememberMultiplePermissionsState. When using rememberPermissionState, there is an issue where if you perform a config change while the permissions dialog is showing, the permissionDeniedContent renders behind the dialog. Because of this, it is preferable to use rememberMultiplePermissionsState when launching the permissions request in onStart.
