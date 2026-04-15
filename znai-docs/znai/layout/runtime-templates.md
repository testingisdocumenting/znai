# URL Query Value

Use `url-query-value` inline plugin to display a value from URL query parameter.
This can be useful for onboarding pages or runbooks where commands or settings change based on user or some runtime 
information, and you can generate a link, providing additional information.

# Inline Syntax

    `:url-query-value: officeName {default: "NYC-15"}`

Renders as: `:url-query-value: officeName {default: "NYC-15"}`

The `officeName` is the query parameter name to read from the URL. 

Click [SF office](layout/runtime-templates?officeName=SF) to see the value above change.

# Missing Value

When no `default` is specified and the query parameter is not present, an error is displayed:

    `:url-query-value: clusterName`

Renders as: `:url-query-value: clusterName`

# Template Syntax In Code Snippets And CLI Commands

Use `${paramName}` or `${paramName:defaultValue}` syntax inside code snippets to substitute values from URL query parameters.
Enable it by setting `templateUseQueryParam: true` on the code block.

    ```shell {templateUseQueryParam: true}
    ssh ${userName:admin}@server-${officeName:NYC}.example.com
    ```

```shell {templateUseQueryParam: true}
ssh ${userName:admin}@server-${officeName:NYC}.example.com
```

    ```cli {templateUseQueryParam: true}
    kubectl config use-context ${officeName:NYC}-cluster 
    ```

```cli {templateUseQueryParam: true}
kubectl config use-context ${officeName:NYC}-cluster 
```

Without query parameters, default values are used. When the page URL contains `?userName=jdoe&officeName=SF`, the substituted values will appear.

Without default values, you get error message when no query parameter is supplied:

    ```cli {templateUseQueryParam: true}
    kubectl config use-context ${myQueryParam}-cluster 
    ```

```cli {templateUseQueryParam: true}
kubectl config use-context ${myQueryParam}-cluster 
```

# Tables With Inline Values

You can use inline `url-query-value` anywhere were text is expected, for example in table cells:

```markdown

| Setting       | Value                                                           |
|---------------|-----------------------------------------------------------------|
| Office        | `:url-query-value: officeName {default: "NYC"}`                 |
| Floor         | `:url-query-value: floorNumber {default: "5"}`                  |
| Wi-Fi Network | `:url-query-value: officeName {default: "NYC"}`-internal        |
| VPN Server    | vpn-`:url-query-value: officeName {default: "NYC"}`.example.com |
```
| Setting       | Value                                                           |
|---------------|-----------------------------------------------------------------|
| Office        | `:url-query-value: officeName {default: "NYC"}`                 |
| Floor         | `:url-query-value: floorNumber {default: "5"}`                  |
| Wi-Fi Network | `:url-query-value: officeName {default: "NYC"}`-internal        |
| VPN Server    | vpn-`:url-query-value: officeName {default: "NYC"}`.example.com |

# Full Example

Click one of the links below to see how values on this page change:

* [SF office, floor 3](layout/runtime-templates?officeName=SF&floorNumber=3&userName=jdoe)
* [NYC office, floor 12](layout/runtime-templates?officeName=NYC&floorNumber=12&userName=admin)
* [London office, floor 7](layout/runtime-templates?officeName=London&floorNumber=7&userName=alice)
* [Default values](layout/runtime-templates)

## Setup Instructions

Welcome to the `:url-query-value: officeName {default: "NYC"}` office!

Connect to the office VPN:

```cli {templateUseQueryParam: true}
sudo vpn connect ${officeName:NYC}-gateway.example.com --user ${userName:admin} 
```

Configure your local environment:

```shell {templateUseQueryParam: true}
export OFFICE=${officeName:NYC}
export FLOOR=${floorNumber:5}
export PRINTER=printer-${officeName:NYC}-${floorNumber:5}
```

Print a test page:

```cli {templateUseQueryParam: true}
lp -d printer-${officeName:NYC}-${floorNumber:5} /etc/motd 
```

Note: If you have a predetermined set of values, consider using [Page Tabs](layout/page-tabs) or [Tabs](layout/tabs) instead