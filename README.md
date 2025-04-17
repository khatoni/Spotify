## 🎶 FMI_SPOTIFY
FMI_SPOTIFY is a simple, Spotify-like client-server application developed in Java as part of a coursework project at the Faculty of Mathematics and Informatics.
It allows users to stream `.wav` music files, search songs, and manage personal playlists — all through a command-line interface.
The application uses sockets for streaming music, enabling real-time audio playback.
It demonstrates key concepts such as:
- Multithreading for handling multiple users and parallel queries
- Real-time streaming of audio data via sockets
- Persistent storage for user data and playlists stored in local files

### 🖥️ Spotify Server
Provides the following services:
- User registration and login
- Song catalog management
- Search songs by title or artist
- Playlists creation and management per user (Each user can have multiple different personal playlists)
- Statistics on most-played tracks
- Streaming `.wav` songs to clients over the network
  ** The server handles multiple users simultaneously, processing queries and streaming songs in parallel using multithreading.
  ** Each user query and song stream is processed in a separate thread

### 💻 Spotify Client
Command-line client interface with support for:
** Interactive commands during song playback realized in separate threads
** Users can search, browse, and manage playlists while streaming music

| Command | Description |
|--------|-------------|
| `register <email> <password>` | Register a new user |
| `login <email> <password>` | Log in with existing credentials |
| `disconnect` | Disconnect from the server |
| `search <query>` | Search for songs by title or artist |
| `top <number>` | Display top *N* most played songs |
| `create-playlist <playlist_name>` | Create a new playlist |
| `add-song-to <playlist_name> <song>` | Add a song to a playlist |
| `show-playlist <playlist_name>` | View songs in a playlist |
| `play <song>` | Stream and play a song |
| `stop` | Stop the current playback |

## 🎧 Music Streaming Details

- All songs on the server must be in **`.wav`** format.
- Streaming is based on `javax.sound.sampled.SourceDataLine`.
- Songs are streamed in real-time — the playback starts without needing to download the entire file.
