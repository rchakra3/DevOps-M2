# M2 - The Big One [AKA Test + Analysis]
The main objective of this milestone is to setup the Testing and Analysis framework for the project. Given below are the different components of the framework.

### Project Used:

We used different branches of [this fork](https://github.com/muchhalsagar88/commons-collections) of the Apache Commons library

### Artifacts:

- [Code Repository](https://github.com/muchhalsagar88/commons-collections)
- [Custom Analysis Script](scripts/codetocomment.py)
- [Pre-commit Hook to prevent uploading secrets](scripts/pre-commit)
- [Pre-commit Code](scripts/TokenParser.java)
- [Screencast YouTube](https://www.youtube.com/watch?v=FMxr9VJU80M&feature=youtu.be)
- [Screencast MP4](/screencast/screencast.mp4)
- [Jenkins Configuration](/config/config.xml)


### Tests

- ***The ability to run unit tests, measure coverage, and report the results:***
    - We used maven to run the unit tests and a plugin called Cobertura to measure the code coverage and create an XML file with the coverage report. 

- ***The ability to improve testing coverage using one of the techniques covered in class: constraint-based test generation, fuzzing, etc. You can use an existing tool or implement your own approach:***
    - For this part, we used a test generation tool called Randoop. It uses a fuzzing technique to generate test cases.
    - The command used was:
    ```
    java -classpath "/home/sagar/randoop-1.3.6.jar:/home/sagar/commons-collections4-4.0.jar" randoop.main.Main gentests --classlist=/home/sagar/classes.txt --timelimit=600 --output-tests=pass
    ```

### Analysis

- ***The ability to run an existing static analysis tool on the source code, process its results, and report its findings:***
    - We used the FindBugs Jenkins plugin for static analysis. This runs on the source code and shows a list of bugs/warnings based on certain rules that can be selected.

- ***The ability to extend an existing analysis tool with a custom analysis, or implement a new analysis from scratch.***
    - After a **huge** number of failed attempts at creating a Custom FindBugs or PMD plugin, we decided to implement our own custom analysis by using a python script in order to:
        - walk through the directories
        - find the JAVA files in our project
        - count the number of lines which are comments
        - count the number of lines which contain logic
        - print the code/comment ratio

- ***Using hooks or post-build scripts, have the ability to reject a commit if it fails a minimum testing criteria and analysis criteria:***
    - The analysis criteria that we are failing on is the (almost definite) case where the code:comment ratio is less than 2.
        - We used a python script for this that we called in the pre-build step in Jenkins. It returns an exit code of 1 if it fails and once this occurs, Jenkins automatically stops the build.
    - The testing criteria is that the amount of conditional coverage of the code is less than 80%:
        - We manually configured the Cobertura plugin to fail the build if this occurs.

- ***The ability to parse a code files and json files in order to detect the presence of AWS/digital ocean security tokens. The ability to check commited files that are private ssh keys. Using hooks, reject the commit if any violation occurs.***
    - Used a simple java code with regex called in the pre-commit hook to parse the source files to detect any access tokens/secret tokens for Digital Ocean or AWS. This returns an exit code of 1 which prevents the *commit* itself.
