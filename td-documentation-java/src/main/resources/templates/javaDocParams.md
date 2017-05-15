<#list params as p>
`````columns {left: {width: 150, align: "right"}, border: true}
left: 
*${p.type}* **${p.name}**

right: 
${p.description}
`````
</#list>
