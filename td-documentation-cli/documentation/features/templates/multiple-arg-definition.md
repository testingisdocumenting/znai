<#list names as n>
```columns {left: {width: 180, align: "right"}, border: true}
left: **${n.name}**
<#if n.optional??>
\
*optional*
</#if>
right: ${n.description}
```
</#list>
