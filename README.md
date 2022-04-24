<p align="center">
    <h1 align="center">Snowow</h1>
    <h6 align="center">A low code service engine built on top of Spring</h6>
</p>

<p align="center">
    <img src="background.png" alt="J" width="1200"/>
</p>

## Introduction
**Snowow is a low code service engine only requires developers to write JSON files to run a web service.** Snowow is built on top of Spring boot,
with the great advantages of MVC framework. Developer does not need to worry about the JAVA code, service is all depended on JSON files.

By writing **JSON** files in the resource folder, snowow would generate Spring code based on the JSON you wrote. Snowow definitely would speed up the
process of backend development, improve code reusability and reduce error.

## Installation

Clone the project into local directory

```bash
    git clone https://github.com/lau1944/snowow.git
```

Open your shell on the project directory and run

```base
    source run.sh
```

Then you can use **Snowow** command to simplify the process

### Initialize the project

```base
    snowow init
```

After that, you will see a `snow_app` folder in your resource folder in the application module.

`api` : This folder is for storing API related files. Files suffix with `.http.json` is the JSON file for HTTP method.

`model` : Declaring Data model in the service

`configuration.json`: Configuratin json file for Spring configuration.

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
      "nullable": 0
    },
    {
      "name": "age",
      "type": "int",
      "nullable": 1,
      "default": 0
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

*Path object*:
`name`: (String) method name

`path`: (String) method path

`API method type`: (String) API method, ex, GET, POST, PUT...

`header`: (String) request header

`params`: (String) request params

`action`: (String) service action

*`response` object*:

`status`: (Int) response status, default `200`

`type`: (String) response type, default `application.json`

*`data` object*:

`type`: Data object type

`value`: (Json) your response data if you want to specify response data, default `null`

Here is the sample `user.http.json` 

```json
{
  "name": "User",
  "version": "1.0",
  "path": "/user",
  "paths": [
    {
      "name": "getUserInfo",
      "path": "/info",
      "method": "GET",
      "header": "",
      "params": "",
      "action": "",
      "response": {
        "status": 200,
        "type": "application/json",
        "data": {
          "type": "User",
          "value": {
            "name": "Jimmy",
            "age": 22,
            "school": [
              {
                "name": "New York University",
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

## Run

After adding the JSON files, type

```base
    snowow run
```

The spring boot application will be compiled and run.


## Future

Snowow is still in the early step of construction, feel free if you are eager to contribute.
