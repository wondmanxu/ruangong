<#macro topics page tags=true>
    <#list page.records as forums>
        <div class="media">
            <a href="/forum/${forums.forumsName!}" class="mr-3"></a>
        </div>
    </#list>
</#macro>
