<p align="center">
    <h1 align="center">Snowow</h1>
    <h6 align="center">A low code service engine built on top of Spring</h6>
</p>

<p align="center">
    <img src="background.png" alt="J" width="1200"/>
</p>

## Introduction

**Snowow is a low code service engine only requires developers to write JSON files to run a web service.** Snowow is
built on top of Spring boot, with the great advantages of MVC framework. Developer does not need to worry about the JAVA
code, service is all depended on JSON files.

By writing **JSON** files in the resource folder, snowow would generate Spring code based on the JSON you wrote. Snowow
definitely would speed up the process of backend development, improve code reusability and reduce error.

## Guide

* [Installation](#Installation)
* [Configuration](#Configuration)
* [Model](#Model)
* [HTTP](#HTTP)
* [Data Form](#data-form)
* [Action](#action)

## Installation

Clone the project into local directory

```bash
    git clone https://github.com/lau1944/snowow.git
```

Open your shell on the project directory and run

```base
    source .snowow.sh
```

Then you can use **Snowow** command to simplify the process

### Initialize the project

```bash
    snowow init
```

After that, you will see a `snow_app` folder in your resource folder in the application module.

`api` : This folder is for storing API related files. Files suffix with `.http.json` is the JSON file for HTTP method.

`model` : Declaring Data model in the service

`configuration.json`: Configuration json file for Spring configuration.

### Configuration

Currently, configuration.json supports these following properties.

`debug`: (Boolean) Set the application mode for service, default to `true`

`appName`: (String) Application name

`port`: (String) Server socket port, default to `80`

`profiles`: (String) Server profiles, will generate application-dev.properties file, default to `dev`

`env`: (Json) additional environment configuration, can be received using `$(env)your_env_name`

Here is the sample configuration file

```json
{
  "debug": true,
  "appName": "SnowApp",
  "profiles": "dev",
  "port": 8080,
  "env": {
    "api_key": "123456"
  }
}
```

### Model

Declare data model in JSON format.

`name`: (String) data model name

`fields`: (Array) array of fields object.

Sample model files of `User.json`

```json
{
  "name": "User",
  "fields": [
    {
      "name": "name",
      "type": "String",
      "nullable": false
    },
    {
      "name": "age",
      "type": "int",
      "nullable": true
    }
  ]
}
```

### HTTP

Declare API in JSON files

`name`: (String) API name

`version`: (String) API version

`path`: (String) API global path

`paths`: (Array) array of `Path` object

#### Path object

`name`: (String) method name

`path`: (String) method path

`API method type`: (String) API method, ex, GET, POST, PUT...

`action`: (JSON) service action (will discuss later)

#### response object:

`status`: (Int) response status, default `200`

`type`: (String) response type, default `application.json`

#### data object:

`type`: Data object type

`value`: (Json) your response data if you want to specify response data, default `null`

Here is the sample `user.http.json`

```json
{
  "name": "User",
  "version": 1.0,
  "path": "/user",
  "paths": [
    {
      "name": "getUserInfo",
      "path": "/info",
      "method": "GET",
      "action": "",
      "response": {
        "status": 200,
        "type": "application/json",
        "data": {
          "type": "User",
          "value": {
            "name": "@{params.name}",
            "age": 22,
            "school": [
              {
                "name": "@{params.school}",
                "age": 4
              },
              {
                "name": "Cornell University",
                "age": 2
              }
            ]
          }
        }
      }
    }
  ]
}
```

### Data form

Each `.http.json` file would generate **one controller class**.

Inside the file, every `path` object inside the `paths` array, we would generate `one HTTP method`, similar to writing
the controller method with `@GetMapping` on it.

Inside the method, we can define `headers` and `params`, which corresponding to our http **RequestHeaders** and **RequestParams**

If you want to receive these value inside the scope of this method, here is format to retrieve the data.

``@{params.your_params_field}`` ``@{headers.your_headers_field}``

Internally, `params` and `headers` will be converted to Map object, calling

`@{params.xx}` is basically `params.get("xx")` in Java.

Similar, `@{headers.xx}` = `headers.get("xx")`

Here is the sample response data in JSON

```json
{
  "data": {
    "type": "Car",
    "value": {
      "age": "@{params.age}",
      "name": "@{headers.name}"
    }
  }
}
```

In the above data, our response will be return the value with key `age` inside `RequestParams`, the value with
key `name` inside `RequestHeaders`.

You can also get your configuration element with this syntax.

```json
{
  "value": {
    "key": "@{configs.env.key}"
  }
}
```

It would get the property `key` in your env object inside your configuration file.

Remember to make sure your JSON reference is correct, if you call `@{config.key}`, but `key` is not in the config
object, you will get a **NullPointer exception** at compile time.

### Action

One of the cool features in *Snowow* is you can play with `action`, it's what make *Snowow* powerful.

Action should be and only be defined in the [path](#path-object).

Action type   | Funtionalities
------------- | -------------
[redirect](#redirect)      | Call the third party API
query | Query from database
insert | Insert data to database
delete | Delete data from database

Currently, only support `redirect` action.


#### Redirect

Sample redirect syntax

```json
"paths": [
    {
        "name": "getBread",
        "path": "/info",
        "method": "GET",
        "action": {
            "type": "redirect",
            "url": "https://api.thecatapi.com/v1/images/search",
            "method": "GET",
            "headers": {
              "x-api-key": "@{configs.env.api_key}"
            },
            "params": {
                "breed_id": "beng"
              }
        },
        "response": {
            "status": 200,
            "type": "application/json",
            "data": {}
        }
    }
]
```

* After you define action type with `redirect`, response data will be ignored, the server will return the response of the third party API call.
* You can use the `@{params.xx}` `@{headers.xx}` data form inside `headers` and `params`

### Run

```bash
    snowow build
```

It would compile the JAVA code and start the Tomcat server.

## Future

Snowow is still in the early step of construction, feel free if you are eager to contribute.
