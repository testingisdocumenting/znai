:include-javascript-function: themeBox {
  label: "${freeForm}",
  severity: "${severity}",
  tags: [<#list tags! as tag>"${tag}"<#sep>, </#list>],
  title: "${severity?upper_case} box"
}
