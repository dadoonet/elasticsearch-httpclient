# HTTP Client call example

This project is a demo project which reproduces a Security Manager issue when we run the plugin in the context
of Elasticsearch as a plugin.

It uses Elasticsearch 6.0.0.

To reproduce:

```sh
mvn clean install
```


* Install elasticsearch 6.0.0
* Remove the existing plugin and install the new one:

```sh
bin/elasticsearch-plugin remove httpclient
bin/elasticsearch-plugin install file:///path/to/elasticsearch-httpclient/target/releases/httpclient-1.0-SNAPSHOT.zip
```

* Start elasticsearch:

```
bin/elasticsearch
```

* Call the REST Endpoint

```
curl 127.0.0.1:9200?pretty
curl 127.0.0.1:9200/_bano?pretty
```

You should see:

```json
{
  "name" : "a5UNyZ-",
  "cluster_name" : "elasticsearch",
  "cluster_uuid" : "Tic8yyC2Q6Gw2AMUAAXwXw",
  "version" : {
    "number" : "6.0.0",
    "build_hash" : "8f0685b",
    "build_date" : "2017-11-10T18:41:22.859Z",
    "build_snapshot" : false,
    "lucene_version" : "7.0.1",
    "minimum_wire_compatibility_version" : "5.6.0",
    "minimum_index_compatibility_version" : "5.0.0"
  },
  "tagline" : "You Know, for Search"
}
{
  "error" : {
    "root_cause" : [
      {
        "type" : "access_control_exception",
        "reason" : "access denied (\"java.net.SocketPermission\" \"127.0.0.1:9200\" \"connect,resolve\")"
      }
    ],
    "type" : "access_control_exception",
    "reason" : "access denied (\"java.net.SocketPermission\" \"127.0.0.1:9200\" \"connect,resolve\")"
  },
  "status" : 500
}
```

