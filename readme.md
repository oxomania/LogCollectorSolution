### Installation

On host machine, these tools must be already installed:
- Docker
- Docker Compose
- Git

Then get the codebase:

```console
$ git clone https://github.com/oxomania/LogCollectorSolution.git
$ ch LogCollectorSolution
```

### Bringing up the stack

Login with your Docker ID to push and pull images from Docker Hub. If you don't have a Docker ID, head over to https://hub.docker.com to create one. Then run command below. It will ask your credentials.

```console
$ docker login
```

Docker Compose will run the stack, start services locally using Docker Compose:

```console
$ docker-compose up
```

When started, it will start adding all existing data in sample_logs/server.log file. Additionally, it will continue adding if you add any extra lines to it.

You can stop started docker-compose group simply typing CTRL+C

You can also run all services in the background (detached mode) by adding the `-d` flag to the above command.

### After Installation

You can visit dashboard from http://localhost


### Cleanup

Elasticsearch data is persisted inside a volume by default.

In order to entirely shutdown the stack and remove all persisted data, use the following Docker Compose command:

```console
$ docker-compose down -v
```

### Using your own server.log

To use your own/live/growing server log file, simply replace "./logstash/pipeline" part of the docker-compose.yml file 
on this full line: "- ./logstash/pipeline:/usr/share/logstash/pipeline:ro" and restart docker-compose.

### Solution Architecture & Design Issues

![swimlanes image](https://static.swimlanes.io/2ad37bc71958c836a9779760509eda4a.png)

Up to given requirements, I designed the system that 
* uses enterprise level tools
* can handle new log sources easily, so the solution allows improvements
* can handle large log files, so has high performance
* fully deployable with simple instructions
* configurable via central script file
* allows multi tenant usage

Given requirements addressed me a statistics database usage would be good for this solution.
Elasticsearch is a special tool for this purpose. I used Logstash to parse log file and sent these log entries to Elasticsearch.

I thought in the context of the application domain, I assumed that this log tracking system will be used by a few users only.
A log watcher probably wants to customise the log view and to be stay flexible for future requirements, I designed the system with polling approach.
An alternative should be designing a watcher on API side and feeding clients via websockets with cached stats. But in this case, user could not change log watch parameters independently.
Instead, user has a freedom to define both "stats refresh period" and "log stats monitoring interval" via dashboard at client side.

This solution is very close to a production ready one and open for enterprise level enrichment requests. 




