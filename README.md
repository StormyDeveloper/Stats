# PlayerStats
This is a Plugin for Spigot. It stores Kills,Deaths,FirstLogin,LastOnline and OnlineTime. <br>
SpigotPage -> https://www.spigotmc.org/resources/%E2%97%8F-playerstats-%E2%97%8F.73506/  <br>

<h2>Are you a developer?</h2>
Then you can use the API of the Plugin.

<h3>How to use the API</h3>
1. First you must import the Plugin in your Project. <br>
2. Second step the Plugins must run on the Server.
<hr>
To call the API use this:<br>

```java
DataManager dm = new DataManager(uuid);
```
<b>Replace "uuid" with a UUID from the player that you want to read or edit the Stats.</b><br>
The Function of the API are in the DataManager class in utils.
