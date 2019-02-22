## Search Service

### Setting up your dev environment

1.  Install docker
    * [Mac users](https://docs.docker.com/docker-for-mac/)
    * [Windows users](https://docs.docker.com/engine/installation/windows/)
    * [Linux users](https://docs.docker.com/engine/installation/linux/ubuntulinux/)

2.  Use this [link](https://docs.docker.com/compose/install/) to install docker compose

3. Please note that setup scripts are compiled for OSX. For Windows change the extension to .bat and tweak the commands/paths necessary for Windows.

### Running the application

1. Use the scripts in docker directory to setup and run the application
    * To setup data execute the script `initial.sh`
    * To run the application execute the script `start.sh`
    * To stop the application execute the script `stop.sh`

### Ports:
1.	Without docker running in IDE/Terminal : Use port 8080
2.	Application running in docker: Use port 8100. Remote debug port 18100


### End points:

#### Keyword search: 
    http://localhost:8100/keywordsearch?keyword=laptop

##### Note: 
    1. Keyword cannot be empty and should match the regex [^*?]+
    2. Failing above rule would result in status 500 with error 

#### Guided search:  Category based search
    http://localhost:8100/search?category1=Apparel

##### Note: Above endpoint fetches products under category Apparel (including any subcategories under Apparel)
    http://localhost:8100/search?category1=Apparel&category2=Shoes

##### Note: Above endpoint fetches products under subcategory Shoes within  Apparel category.

### Running Tests

1. To run unit tests run

    `./gradlew clean test`


### Command to build docker image:

#### Use one of below commands  to genetate docker image for search-service application.
    1. Using docker-compose, execute below commands from project root
        a. ./gradlew clean build
        b. docker-compose -f ./docker/docker-compose.yml build search-service
    2. Using Dockerfile. Execute below commands from project root
        a. ./gradlew clean build
        b. docker build .
        `Note: do not forget DOT in command 2.b command in the end`
    3. Using shell script (verified only in OSX)
        a. Execute start.sh under docker directory


