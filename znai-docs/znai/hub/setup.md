# Config File

Create the enterprise config file and set the path where Znai will store uploaded documentation:
```json {title: "znai-enterprise.cfg"}
{
  "znaiDocStoragePath": "/path/to-store-docs-to-serve"
}
```

# Run In Server Mode

Run Znai in server mode in the directory with the config file:
```cli
znai --serve --port=<port>
```

# Upload Documentation

Set `ZNAI_SERVER_URL` environment variable to the full url of the server, e.g.:
```cli
export ZNAI_SERVER_URL=http://localhost:7070
```

Now you can run `upload` command to build and upload a documentation:
```cli
znai --deploy=/path/where/docresources/generate --doc-id=my-doc-id --upload
```
