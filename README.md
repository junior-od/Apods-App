# 
Please keep your Git history intact as we are interested in your development process as well as the end result.
Feel free to make changes to any code in this repository.

Your task is to create an app using the NASA Astronomy Picture Of the Day (APOD) API.
We have set up some basic networking code; you still need to register for an [API key](https://api.nasa.gov/) 
and add it to `app/local.gradle` - see `local.gradle.example` for details. 
You can verify your API key works by running the `PlanetaryServiceTest`.

## Features:
- [ ] Create a list screen of APODs ![](screens/List Screen.png):
  * Sanitise list to ONLY show images - NOT videos.
- [ ] Implement ordering for APODs ![](screens/Reorder Dialog.png):
  * Order by title - ascending.
  * Order by date - descending.
- [ ] Show a details screen with more info about a single APOD ![](screens/Detail Screen.png).
- [ ] Implement error screens:
  * Api error ![](screens/Error screen.png).

n.b The resources folder has been updated with the appropriate colors and icons.
  
## Development Tasks:
Tips for candidates to think about.

- [ ] App to function in both portrait and landscape orientations:
   * Is the UI still functional - is state persisted?
   * Ensure unnecessary network calls are not made.
- [ ] Unit tests.
