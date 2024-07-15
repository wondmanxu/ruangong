<#macro header page_tab>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <#if forumsName?? && forumsName != "">
            <a class="navbar-brand" href="/search?forumsName=${forumsName!}&keyword=''">${forumsName}</a>
        <#else>
            <a class="navbar-brand" href="/" onclick="clearForumsName()">选课论坛</a>
        </#if>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <ul class="navbar-nav">
            <li class="nav-item <#if page_tab == "index">active</#if>">
                <a href="/" class="nav-link" onclick="clearForumsName()">
                    <i class="fa fa-home"></i> ${i18n.getMessage("index")}
                </a>
            </li>
<#--            <li class="nav-item <#if page_tab == "tags">active</#if>">-->
<#--                <a href="/tags" class="nav-link">-->
<#--                    <i class="fa fa-tags"></i> ${i18n.getMessage("tag")}-->
<#--                </a>-->
<#--            </li>-->
        </ul>
        <div class="collapse navbar-collapse justify-content-between" id="navbarSupportedContent">
            <div class="d-flex justify-content-start">
                <form class="form-inline my-2 my-lg-0 ml-2 d-none d-md-block" action="/search">

                    <div class="input-group">
<#--                        <input class="form-control" type="search" name="forumsName" placeholder="搜索课程" value="${forumsName!}"-->
<#--                               required aria-label="Search Bar">-->
                        <select class="form-control" name="forumsName" aria-label="Search Bar" onchange="saveToLocalStorage()">
<#--                            <#if forumsName?? && forumsName != "">-->
<#--                                <option value="${forumsName}">${forumsName}</option>-->
<#--                            <#else>-->
<#--                                <option value="">请选择课程</option>-->
<#--                            </#if>-->
                            <option value="">请选择课程</option>
                            <#if forumsNames??>
                                <#list forumsNames as forumsName>
                                    <option value="${forumsName}">${forumsName}</option>
                                </#list>
                            <#else>
                                <option value="">没有可选课程</option>
                            </#if>
                        </select>
                        <input class="form-control" type="search" name="keyword" placeholder="搜索帖子" value="${keyword!}"
                               aria-label="Search" onchange="saveToLocalStorage()">
                        <div class="input-group-append">
                            <button class="btn btn-outline-success" type="submit">${i18n.getMessage("search")}</button>
                        </div>
                    </div>
                </form>
            </div>
            <ul class="navbar-nav">
                <#if _user??>
                    <li class="nav-item <#if page_tab == "notification">active</#if>">
                        <a href="/notifications" class="nav-link">
                            <i class="fa fa-envelope"></i> ${i18n.getMessage("notification")}
                            <span class="badge badge-default" id="nh_count"></span>
                        </a>
                    </li>
                    <li class="nav-item <#if page_tab == "user">active</#if>">
                        <a href="/user/${_user.username}" class="nav-link">
                            <i class="fa fa-user"></i> ${_user.username}
                        </a>
                    </li>
<#--                    <li class="nav-item <#if page_tab == "settings">active</#if>">-->
<#--                        <a href="/settings" class="nav-link">-->
<#--                            <i class="fa fa-cog"></i> ${i18n.getMessage("setting")}-->
<#--                        </a>-->
<#--                    </li>-->
                    <li class="nav-item">
                        <a href="javascript:if(confirm('确定要登出吗？登出了就没办法发帖回帖了哦!'))window.location.href='/logout'"
                           class="nav-link" onclick="clearForumsName()">
                            <i class="fa fa-sign-out"></i> ${i18n.getMessage("logout")}
                        </a>
                    </li>
                <#else>
                    <li class="nav-item <#if page_tab == "login">active</#if>">
                        <a href="/login" class="nav-link" onclick="clearForumsName()">
                            <i class="fa fa-sign-in"></i> ${i18n.getMessage("login")}
                        </a>
                    </li>
                </#if>
            </ul>
        </div>
    </nav>
    <script>
        // Function to get value from localStorage or default value
        function getLocalStorageOrDefault(key, defaultValue) {
            return localStorage.getItem(key) || defaultValue;
        }

        // Set initial values from localStorage
        document.addEventListener("DOMContentLoaded", function() {
            // Set the forumsName
            const forumsName = getLocalStorageOrDefault('forumsName', '');
            const forumsNameSelect = document.querySelector('select[name="forumsName"]');
            if (forumsNameSelect) {
                forumsNameSelect.value = forumsName;
            }

            // Set the keyword
            const keyword = getLocalStorageOrDefault('keyword', '');
            const keywordInput = document.querySelector('input[name="keyword"]');
            if (keywordInput) {
                keywordInput.value = keyword;
            }

            // 更新页面上显示的forumsName
            // updateNavbarBrand(forumsName);
        });

        // // 更新导航栏中显示的forumsName
        // function updateNavbarBrand(forumsName) {
        //     const navbarBrand = document.querySelector('.navbar-brand');
        //     if (forumsName != null && forumsName !== ''){
        //         let tmp = '/search?forumsName=' + forumsName + '&keyword= ';
        //         navbarBrand.href = tmp;
        //         navbarBrand.textContent = forumsName;
        //     } else {
        //         navbarBrand.href = '/';
        //         navbarBrand.textContent = '选课论坛';
        //     }
        // }


        // Save the selected forumsName and keyword to localStorage on change
        function saveToLocalStorage() {
            const forumsName = document.querySelector('select[name="forumsName"]').value;
            const keyword = document.querySelector('input[name="keyword"]').value;
            localStorage.setItem('forumsName', forumsName);
            localStorage.setItem('keyword', keyword);
        }
        function clearForumsName() {
            const forumsName = document.querySelector('select[name="forumsName"]').value;
            const keyword = document.querySelector('input[name="keyword"]').value;
            localStorage.setItem('forumsName', "");
            localStorage.setItem('keyword', "");
        }
    </script>
</#macro>
