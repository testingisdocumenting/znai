# Command parameters

To bring attention to important parameters in your examples use `cli-command` extension.

    :include-cli-command: my-super-command paramA --important-flag=true {paramToHighlight: "important"}
    
:include-cli-command: my-super-command paramA --important-flag=true {paramToHighlight: "important"}

Note: parameter name gets matched as long as its name contains passed value

Use plural version `paramsToHighlight` to highlight multiple parameters
 
    :include-cli-command: another-command --score=2 --name=Name --value=8 {paramsToHighlight: ["name", "value"]}

:include-cli-command: another-command --score=2 --name=Name --value=8 {paramsToHighlight: ["name", "value"]}

# Long lines

Long command lines will be automatically wrapped into multiple lines

:include-cli-command: another-command --score=2 --name=Name --value=8 --long-parameter-test --another-long-parameter1 --another-long-parameter2 --another-long3 {paramsToHighlight: ["name", "value"]}


# Presentation mode

Entire command will be revealed as if typed inside terminal. All parameters will be highlighted immediately. 

If you want one parameter revealed at a time let us know via [JIRA](https://jira/mdoc)



