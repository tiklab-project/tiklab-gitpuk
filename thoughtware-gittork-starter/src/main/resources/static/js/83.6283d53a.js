(window.webpackJsonp=window.webpackJsonp||[]).push([[83],{1339:function(e,t,r){"use strict";r.r(t);var n=r(0),u=r.n(n),l=r(651),a=r(640),o=(r(216),r(194)),c=(r(73),r(32)),i=(r(74),r(27)),m=(r(197),r(57)),s=(r(50),r(18)),f=(r(60),r(15)),b=r(220),d=r(646),N="/Users/limingliang/xcode-xpack/thoughtware-gittork-ui/src/repository/setting/webHooks/components/HooksAdd.js";function _(e,t){return function(e){if(Array.isArray(e))return e}(e)||function(e,t){var r=null==e?null:"undefined"!=typeof Symbol&&e[Symbol.iterator]||e["@@iterator"];if(null!=r){var n,u,l,a,o=[],c=!0,i=!1;try{if(l=(r=r.call(e)).next,0===t){if(Object(r)!==r)return;c=!1}else for(;!(c=(n=l.call(r)).done)&&(o.push(n.value),o.length!==t);c=!0);}catch(e){i=!0,u=e}finally{try{if(!c&&null!=r.return&&(a=r.return(),Object(a)!==a))return}finally{if(i)throw u}}return o}}(e,t)||function(e,t){if(!e)return;if("string"==typeof e)return p(e,t);var r=Object.prototype.toString.call(e).slice(8,-1);"Object"===r&&e.constructor&&(r=e.constructor.name);if("Map"===r||"Set"===r)return Array.from(e);if("Arguments"===r||/^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(r))return p(e,t)}(e,t)||function(){throw new TypeError("Invalid attempt to destructure non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method.")}()}function p(e,t){(null==t||t>e.length)&&(t=e.length);for(var r=0,n=new Array(t);r<t;r++)n[r]=e[r];return n}var v=function(e){var t=e.addVisible,r=e.setAddVisible,l=_(f.default.useForm(),1)[0],p=_(Object(n.useState)(0),2),v=p[0],y=p[1];Object(n.useEffect)((function(){return y(Object(d.b)()),function(){window.onresize=null}}),[v]),window.onresize=function(){y(Object(d.b)())};var h=u.a.createElement(u.a.Fragment,null,u.a.createElement(a.a,{onClick:function(){return r(!1)},title:"取消",isMar:!0,__source:{fileName:N,lineNumber:31,columnNumber:13}}),u.a.createElement(a.a,{onClick:function(){l.validateFields().then((function(e){l.resetFields()}))},title:"确定",type:"primary",__source:{fileName:N,lineNumber:36,columnNumber:13}}));return u.a.createElement(o.default,{open:t,onCancel:function(){return r(!1)},closable:!1,footer:h,style:{height:v,top:60},bodyStyle:{padding:0},className:"xcode hooks-add-modal",destroyOnClose:!0,__source:{fileName:N,lineNumber:76,columnNumber:9}},u.a.createElement("div",{className:"hooks-add-up",__source:{fileName:N,lineNumber:86,columnNumber:13}},u.a.createElement("div",{__source:{fileName:N,lineNumber:87,columnNumber:17}},"新建WebHooks"),u.a.createElement("div",{style:{cursor:"pointer"},onClick:function(){return r(!1)},__source:{fileName:N,lineNumber:88,columnNumber:17}},u.a.createElement(b.default,{__source:{fileName:N,lineNumber:89,columnNumber:21}}))),u.a.createElement("div",{className:"hooks-add-content",__source:{fileName:N,lineNumber:92,columnNumber:13}},u.a.createElement(f.default,{form:l,layout:"vertical",autoComplete:"off",initialValues:{2:2},__source:{fileName:N,lineNumber:93,columnNumber:17}},u.a.createElement(f.default.Item,{label:"URL",name:"1",rules:[{required:!0,message:"URL不能为空"}],__source:{fileName:N,lineNumber:99,columnNumber:21}},u.a.createElement(s.default,{placeholder:"POST地址",__source:{fileName:N,lineNumber:102,columnNumber:25}})),u.a.createElement(f.default.Item,{label:"选择事件",name:"safa",rules:[{required:!0,message:"选择事件不能为空"}],__source:{fileName:N,lineNumber:104,columnNumber:21}},u.a.createElement(m.default.Group,{__source:{fileName:N,lineNumber:107,columnNumber:25}},u.a.createElement(c.default,{__source:{fileName:N,lineNumber:108,columnNumber:29}},[{value:"Push",desc:"仓库推送代码、推送、删除分支"},{value:"Tag Push",desc:"新建、删除tag"},{value:"Issue",desc:"新建任务、删除任务、变更任务状态、更改任务指派人"},{value:"Pull Request",desc:"新建、更新、合并、关闭Pull Request，新建、更新、删除Pull Request下便签，关联、取消关联Issue"},{value:"评论",desc:"评论仓库、任务、Pull Request、Commit"}].map((function(e){return u.a.createElement(i.default,{span:24,key:e.value,__source:{fileName:N,lineNumber:111,columnNumber:49}},u.a.createElement(m.default,{value:e.value,style:{lineHeight:"32px"},__source:{fileName:N,lineNumber:112,columnNumber:45}},u.a.createElement("div",{className:"event-check",__source:{fileName:N,lineNumber:113,columnNumber:49}},u.a.createElement("div",{className:"event-check-value",__source:{fileName:N,lineNumber:114,columnNumber:53}},e.value),u.a.createElement("div",{className:"event-check-desc",__source:{fileName:N,lineNumber:115,columnNumber:53}},e.desc))))}))))),u.a.createElement(f.default.Item,{__source:{fileName:N,lineNumber:124,columnNumber:21}},u.a.createElement(m.default,{style:{lineHeight:"32px"},__source:{fileName:N,lineNumber:125,columnNumber:25}},u.a.createElement("div",{className:"activate",__source:{fileName:N,lineNumber:126,columnNumber:29}},u.a.createElement("span",{className:"activate-title",__source:{fileName:N,lineNumber:127,columnNumber:33}},"激活"),u.a.createElement("span",{className:"activate-desc",__source:{fileName:N,lineNumber:128,columnNumber:33}},"（激活后事件触发时将发送请求）")))),u.a.createElement(f.default.Item,{label:"描述信息",name:"3",__source:{fileName:N,lineNumber:132,columnNumber:21}},u.a.createElement(s.default.TextArea,{__source:{fileName:N,lineNumber:133,columnNumber:25}})))))},y="/Users/limingliang/xcode-xpack/thoughtware-gittork-ui/src/repository/setting/webHooks/components/Hooks.js";function h(e,t){return function(e){if(Array.isArray(e))return e}(e)||function(e,t){var r=null==e?null:"undefined"!=typeof Symbol&&e[Symbol.iterator]||e["@@iterator"];if(null!=r){var n,u,l,a,o=[],c=!0,i=!1;try{if(l=(r=r.call(e)).next,0===t){if(Object(r)!==r)return;c=!1}else for(;!(c=(n=l.call(r)).done)&&(o.push(n.value),o.length!==t);c=!0);}catch(e){i=!0,u=e}finally{try{if(!c&&null!=r.return&&(a=r.return(),Object(a)!==a))return}finally{if(i)throw u}}return o}}(e,t)||function(e,t){if(!e)return;if("string"==typeof e)return E(e,t);var r=Object.prototype.toString.call(e).slice(8,-1);"Object"===r&&e.constructor&&(r=e.constructor.name);if("Map"===r||"Set"===r)return Array.from(e);if("Arguments"===r||/^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(r))return E(e,t)}(e,t)||function(){throw new TypeError("Invalid attempt to destructure non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method.")}()}function E(e,t){(null==t||t>e.length)&&(t=e.length);for(var r=0,n=new Array(t);r<t;r++)n[r]=e[r];return n}t.default=function(e){var t=h(Object(n.useState)(!1),2),r=t[0],o=t[1];return u.a.createElement("div",{className:"hooks",__source:{fileName:y,lineNumber:12,columnNumber:9}},u.a.createElement("div",{className:"hooks-content xcode-repository-width-setting xcode",__source:{fileName:y,lineNumber:13,columnNumber:13}},u.a.createElement("div",{className:"hooks-up",__source:{fileName:y,lineNumber:14,columnNumber:17}},u.a.createElement(l.a,{firstItem:"WebHooks",__source:{fileName:y,lineNumber:15,columnNumber:21}}),u.a.createElement(a.a,{type:"primary",title:"添加WebHooks",onClick:function(){return o(!0)},__source:{fileName:y,lineNumber:16,columnNumber:21}}),u.a.createElement(v,{addVisible:r,setAddVisible:o,__source:{fileName:y,lineNumber:21,columnNumber:21}})),u.a.createElement("div",{className:"hooks-illustrate",__source:{fileName:y,lineNumber:26,columnNumber:17}},u.a.createElement("div",{__source:{fileName:y,lineNumber:27,columnNumber:21}},"每次您 push 代码后，都会给远程 HTTP URL 发送一个 POST 请求 更多说明 »"),u.a.createElement("div",{__source:{fileName:y,lineNumber:29,columnNumber:21}},"WebHook 增加对钉钉的支持 更多说明 »"),u.a.createElement("div",{__source:{fileName:y,lineNumber:31,columnNumber:21}},"WebHook 增加对企业微信的支持 更多说明 »"),u.a.createElement("div",{__source:{fileName:y,lineNumber:33,columnNumber:21}},"WebHook 增加对飞书的支持 更多说明 »"))))}},301:function(e,t){e.exports=function(e){return e.webpackPolyfill||(e.deprecate=function(){},e.paths=[],e.children||(e.children=[]),Object.defineProperty(e,"loaded",{enumerable:!0,get:function(){return e.l}}),Object.defineProperty(e,"id",{enumerable:!0,get:function(){return e.i}}),e.webpackPolyfill=1),e}},646:function(e,t,r){"use strict";r.d(t,"b",(function(){return a})),r.d(t,"c",(function(){return o})),r.d(t,"d",(function(){return c})),r.d(t,"a",(function(){return i}));r(71);var n=r(31),u=r(118),l=r.n(u),a=(l()().format("YYYY-MM-DD HH:mm:ss"),l()().format("HH:mm"),function(){var e=0;return window.innerHeight?e=window.innerHeight:document.body&&document.body.clientHeight&&(e=document.body.clientHeight),document.documentElement&&document.documentElement.clientHeight&&(e=document.documentElement.clientHeight),e-120}),o=function(e){var t=e,r=document.createElement("input");document.body.appendChild(r),r.setAttribute("value",t),r.select(),document.execCommand("Copy"),n.default.success("复制成功"),r.remove()},c=function(e,t){return t?e.split("/repository/"+t):e.split("/")},i=function(e,t){return"blank"===t?{pattern:/^[^\s]*$/,message:"".concat(e,"不能包含空格")}:"appoint"===t?{pattern:/^[a-zA-Z0-9_]([a-zA-Z0-9_\-.])*$/,message:"只能包含字母和数字、 '_'、 '.'和'-'，且只能以字母、数字或'_'开头"}:{pattern:/^[\u4e00-\u9fa5a-zA-Z0-9_-]{0,}$/,message:"".concat(e,"不能包含非法字符，如&,%，&，#……等")}}},671:function(e,t,r){var n={"./es":653,"./es-do":654,"./es-do.js":654,"./es-mx":655,"./es-mx.js":655,"./es-us":656,"./es-us.js":656,"./es.js":653,"./zh-cn":657,"./zh-cn.js":657};function u(e){var t=l(e);return r(t)}function l(e){if(!r.o(n,e)){var t=new Error("Cannot find module '"+e+"'");throw t.code="MODULE_NOT_FOUND",t}return n[e]}u.keys=function(){return Object.keys(n)},u.resolve=l,e.exports=u,u.id=671}}]);