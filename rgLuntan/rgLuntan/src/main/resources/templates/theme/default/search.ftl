<#include "layout/layout.ftl"/>
<@html page_title="首页" page_tab="index">
    <div class="row">
        <div class="col-md-9">
            <div class="card">
                <div class="card-header">
                    <ul class="nav nav-pills">
                        <li class="nav-item"><a class="nav-link <#if tab=="all">active</#if>" href="/search?tab=all&forumsName=${forumsName!}&keyword=${keyword!}">全部</a></li>
                        <#--                        <li class="nav-item"><a class="nav-link <#if tab=="good">active</#if>" href="/?tab=good">精华</a></li>-->
                        <li class="nav-item"><a class="nav-link <#if tab=="hot">active</#if>" href="/search?tab=hot&forumsName=${forumsName!}&keyword=${keyword!}">最热</a></li>
                        <li class="nav-item"><a class="nav-link <#if tab=="newest">active</#if>" href="/search?tab=newest&forumsName=${forumsName!}&keyword=${keyword!}">最新</a></li>
<#--                        <li class="nav-item"><a class="nav-link <#if tab=="label">active</#if>" href="/tags?forumsName=${forumsName!}&keyword=${keyword!}">标签</a></li>-->
                        <#--                        <li class="nav-item"><a class="nav-link <#if tab=="noanswer">active</#if>" href="/?tab=noanswer">无人问津</a></li>-->
                    </ul>
                </div>
                <div class="card-body">
                    <@tag_search pageNo=pageNo tab=tab!"all" forumsName=forumsName keyword=keyword>
                        <#include "components/topics.ftl"/>
                        <@topics page=page/>
                        <#include "components/paginate.ftl"/>
                        <@paginate currentPage=page.current totalPage=page.pages actionUrl="/search" urlParas="&forumsName=${forumsName!}&keyword=${keyword!}&tab=${tab!}"/>
                    </@tag_search>
                </div>
            </div>
        </div>
        <div class="col-md-3 d-none d-md-block">
            <#if _user??>
                <#include "components/user_info.ftl"/>
            <#else>
                <#include "components/welcome.ftl"/>
            </#if>
        </div>
    </div>
</@html>
