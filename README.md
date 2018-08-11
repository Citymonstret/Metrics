## Running

The server relies on a MongoDB connection. The connection details
can be configured in `./kvantum/config/server.yml`. It is important
that the server is launched using the MongoDB database implementation
as the application internals rely on that connection.

## Development

### Code Style

If you are planning to commit any changes to the project,
it would be highly appreciated if you were to follow the 
project code style conventions. To make this easier we have
provided settings that can be imported into your IDE.

**Eclipse:**
`Window > Preferences > Java > Code Style > Formatter`
Press `Import` and select `...path/to/project/code_style_eclipse.xml`

**IntelliJ:**
`File > Settings > Editor > Code Style`. Next to "Scheme" there is a cog wheel, press that and then
`Import Scheme > IntelliJ IDEA Code Style XML` and then select `..path/to/project/code_style_intellij.xml`
