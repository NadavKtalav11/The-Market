# The-Market
Digital Costumer To Customer Market 
Draw.io - https://drive.google.com/file/d/1jqB_yrgaZJWGNDhOd9AUbR-pm6U9uRU0/view?usp=sharing

Use-Cases docs sheet - https://docs.google.com/document/d/1lFxkYdhTieLjeW3Bd95CnTiDuwqNHhB1Q3NCLC6515Y/edit?usp=sharing

Glossary - https://docs.google.com/document/d/1cLOtKh-OQUaAp9FVlCy0EocshTjExcrV7qxI3wZly34/edit

# The program initialization:
The program initialization is an automatic process that runs when the program is opned for the first time. 

The automatic process runs the commands in the Configuration and stateAfterInit files.

The system state after running it should be according to this files.

# How the Configuration file should be written
  First, it should be a yaml file called "ConfigurationFile".
  
  It has to be located at: src/main/resources/ConfigurationFile.yaml

  The file has to include the following details:
  
    - Details of the default system manager:
    
      * username: The user name of the default system manager.
      * password: The password of the default system manager.
      * birthday: The birthday of the default system manager.
      * country: The country of the default system manager.
      * city: The city of the default system manager.
      * address: The address of the default system manager.
      * name: The name of the default system manager.


