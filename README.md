#Auteur : Lionel Ding


---------
FPS / UPS
---------
Max UPS and FPS can be changed using these methods :
- gc.setMaxFPS();
- gc.setMaxUPS();


-------------
Console Color
-------------
When writing to the server/client console, using a special syntax enables you to add colors.
 - Each String is divided into sections by blank spaces
 - Each section that stats with a # defines a changement in the current color
 - The new current color is defined by the following character
 - Here's the list of colors :
    r => RED
    g => GREEN
    b => blue
    y => yellow
    c => CYAN
    o => ORANGE
    m => MAGENTA
    p => PINK
    default,
    w,
    - => white  
Exemple :
White White #bBlue Blue #-White #rRed Red Red #oOrange


-------------
Event History
-------------
To add a event in the history of the current app, use " gc.getEventHistory().addEvent()"


-------
Buttons
-------
To create a button, use the class "Button" (new Button(x,y,"name"))
Here are the customizable things :
- Font (button.setFont())
- Listener (button.addListener())
- Width (button.setWidth())
- Height (button.setHeight())

Ensuite, avec gc.getWindow().addButton(), l'on peut ajouter le boutton sur l'ihm


------------
Custom Panel
------------
It is possible to create custom panels, add them to the game engine and switch from one to another.
A custom panel class must extends "CustomPanel" and override the paintComponent method.
Then, to add a custom panel to the GameContainer :
 - gc.getWindow().addPanel("name", panel);
And to switch to it :
 - gc.getWindow().switchPanel("name");

To switch to the default panel (the main one), use "main".


-----
Menus
-----
To create a menu, use the class "Menu"
Here are the customizable things :
- Title (menu.setTitle())
- Title Font (menu.setTitleFont())
- Add buttons (menu.addButton())
- Button Font (menu.setButtonFont())
- Background Image (menu.setBackgroundImage())
!! The button font needs to be set BEFORE the buttons are added !!

Similary to the custom panel, a menu can be added using :
- gc.getWindow().addPanel("name", menu);
And switch to using :
 - gc.getWindow().switchPanel("name");
To go back to the default panel, use "main" 



-------
Cursors
-------
To change the cursors, use "gc.getWindow().getCursorManager().setCursor(CursorType, Weight)"
Depending on the weight of the request, the cursor will or will not change.
There is some default weight that can be accessed using "CursorOrder.XXX".
A lower weight will override the current cursor

setCursor returns "true" if the cursor has been changed


---
Cam
---
To modify the properties of the camera, use gc.getCam() :
 - .setCamLimit(minX,minY,maxX,maxY)
 - .setDragEnabled(boolean)
 - .setFocus(GameObject)
 - .setZoomEnabled(boolean)
 - .setZoomLimits(min, max)
 - .setXFocus(GameObject)
 - .setYFocus(GameObject)

-------------
Camera Motion
-------------
To add a dynamic camera, create a new CameraMotion passing the GameContainer as paramter
Then :
- .setGameObjects() => adds the list of players to follow
- .setMarginX()     => sets the X margin to place between each side and the closest gameObject  
- .setMarginY()     => sets the Y margin to place between the top and bottom and the closest gameObject  
- .setMinWidth()    => sets the minimum width (in pixel) that the zoom (scale) can achieve
- .setMinHeight()   => sets the minimum height (in pixel) that the zoom (scale) can achieve
- .setMaxWidth()    => sets the maximum width (in pixel) that the zoom (scale) can achieve
- .setMaxHeight()   => sets the maximum height (in pixel) that the zoom (scale) can achieve
- .setCamSpeed()     => sets the speed of the camera to move (a smaller number will give a faster camera (distance / speed))


-----
Title
-----
To change the title, use "gc.getSettings().setTitle()"


-----
Graph
-----
To use a graph, first create one (new Graph) passing it's position
Then use these methods :
 - .addData(String, Color) -- Defines a new DataSet with a name (String) and a Color
 - .setVisible(boolean) -- Show/Hide the graph
 - .setAverage(String, int) -- Defines the average lvl (int) of a certain DataSet (String)
 - .setXPointLimit(int) -- Defines the maximum number of points rederer on the X axes
 - .setCamSensitive(boolean) -- Prevents the graph from being moved or zoomed in/out
 - .setYGraduation(int) -- Default false
 - .setAxesMargin(int) -- Sets the margin under the X axes and to the left of the y axes
 - .setFixedMaxYValue(int) -- Defines a maximum on the Y axes, by default there is none and the graduation updates according to the data
 - .setOriginYValue(int) -- Defines the origin of the Y axes
 - .setSmoothDataMovement(boolean) -- Enable/Disable smooth movement of the data on the graph. Can cause the graph to be weirdly rendered.
 - .setShowValuesOnMouse(boolean) -- Show/Hide values when hoverring with the mouse (Default hide)
 - .setShowValues(boolean) -- Always Show/Hide values (Default hide)
 - .setGraphXOffset(int) -- Sets an offset in the list of shown point to go back in time
 

------
Server
------
To enable the server, use "gc.enableServer(NetType.SERVER)
Here are the customizable things :
- gc.getSettings().setServerName()
- gc.getSettings().setGameVersion()
- gc.getServer().addEventListener(new ServerEventController())
- gc.getServer().sendTCPPacket()
- gc.getServer().sendUDPPacket()
- gc.getServer().enableNetworkSecurity(NetworkSecurityType.TYPE)


-------------
Music / Sound
-------------
Create a music object using the class "SoundClip"
The constructor needs 2 arguments :
1. (String) The file name (./assets/audios) isn't requierd, it is automatically added
2. (float) The volume (in DCBL, so negativ numbers are okay (and recommanded)
Then :
- .play() -- Starts the audio
- .stop() -- Stops the audio
- .close() -- Deletes the audio
- .loop() -- Loops the audio
