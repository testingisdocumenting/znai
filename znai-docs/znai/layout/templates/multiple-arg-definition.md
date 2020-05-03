<#list names as n>
```columns {left: {portion: 3, align: "right"}, border: true}
left: **${n.name}**
<#if n.optional??>
\
*optional*
</#if>
right: ${n.description}
```
</#list>
