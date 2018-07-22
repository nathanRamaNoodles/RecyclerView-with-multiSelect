# RecyclerView-with-multiSelect
[Here is the video of the app's problem](https://www.youtube.com/watch?v=9HueknubtHI)

I need help to make the app keep the contextual actionBar on rotation.  I know the dirty way is to put  `android:configChanges="orientation|screenSize"` in the AndroidManifest file.  But this will cause the entire activity to never destroy itself; therefore, not giving me options to give different Views for landscape/portrait in other fragments in the same activity.  I am using fragments, so I want a way to never destroy the main activity only when the CrimeListFragment is open for the user; **however**, I don't care if the main activity is recreated for the other fragments.

**Essentially**, I want a way to retain the state of a fragment.  I tried to use `setRetainInstance(true)` in `onCreate()` but it didn't work :(
