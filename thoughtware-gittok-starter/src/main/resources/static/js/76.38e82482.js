(window.webpackJsonp=window.webpackJsonp||[]).push([[76,8],{1217:function(e,t,n){"use strict";n.r(t);n(511);var r=n(512),i=n.n(r),o=n(0),c=n.n(o),a=n(58),s="/Users/limingliang/xcode-xpack/web/thoughtware-gittok-ui/src/repository/setting/user/RepositoryUser.js";function u(){return(u=Object.assign?Object.assign.bind():function(e){for(var t=1;t<arguments.length;t++){var n=arguments[t];for(var r in n)Object.prototype.hasOwnProperty.call(n,r)&&(e[r]=n[r])}return e}).apply(this,arguments)}t.default=Object(a.inject)("repositoryStore")(Object(a.observer)((function(e){var t=e.match,n=e.repositoryStore,r=n.findRepositoryByAddress,a=n.repositoryInfo,l="".concat(t.params.namespace,"/").concat(t.params.name);return Object(o.useEffect)((function(){r(l)}),[]),c.a.createElement("div",{__source:{fileName:s,lineNumber:22,columnNumber:9}},c.a.createElement(i.a,u({},e,{domainId:a.rpyId,bgroup:"gittok",__source:{fileName:s,lineNumber:23,columnNumber:13}})))})))},138:function(e,t){e.exports=function(e,t,n,r){var i=n?n.call(r,e,t):void 0;if(void 0!==i)return!!i;if(e===t)return!0;if("object"!=typeof e||!e||"object"!=typeof t||!t)return!1;var o=Object.keys(e),c=Object.keys(t);if(o.length!==c.length)return!1;for(var a=Object.prototype.hasOwnProperty.bind(t),s=0;s<o.length;s++){var u=o[s];if(!a(u))return!1;var l=e[u],p=t[u];if(!1===(i=n?n.call(r,l,p,u):void 0)||void 0===i&&l!==p)return!1}return!0}},190:function(e,t){function n(){return e.exports=n=Object.assign?Object.assign.bind():function(e){for(var t=1;t<arguments.length;t++){var n=arguments[t];for(var r in n)Object.prototype.hasOwnProperty.call(n,r)&&(e[r]=n[r])}return e},e.exports.__esModule=!0,e.exports.default=e.exports,n.apply(this,arguments)}e.exports=n,e.exports.__esModule=!0,e.exports.default=e.exports},198:function(e,t,n){"use strict";function r(e){if(null==e)throw new TypeError("Cannot destructure "+e)}n.d(t,"a",(function(){return r}))},299:function(e,t){e.exports=function(e){return e.webpackPolyfill||(e.deprecate=function(){},e.paths=[],e.children||(e.children=[]),Object.defineProperty(e,"loaded",{enumerable:!0,get:function(){return e.l}}),Object.defineProperty(e,"id",{enumerable:!0,get:function(){return e.i}}),e.webpackPolyfill=1),e}},696:function(e,t){e.exports={area:!0,base:!0,br:!0,col:!0,embed:!0,hr:!0,img:!0,input:!0,link:!0,meta:!0,param:!0,source:!0,track:!0,wbr:!0}},762:function(e,t,n){"use strict";var r=n(696),i=n.n(r),o=/\s([^'"/\s><]+?)[\s/>]|([^\s=]+)=\s?(".*?"|'.*?')/g;function c(e){var t={type:"tag",name:"",voidElement:!1,attrs:{},children:[]},n=e.match(/<\/?([^\s]+?)[/\s>]/);if(n&&(t.name=n[1],(i.a[n[1]]||"/"===e.charAt(e.length-2))&&(t.voidElement=!0),t.name.startsWith("!--"))){var r=e.indexOf("--\x3e");return{type:"comment",comment:-1!==r?e.slice(4,r):""}}for(var c=new RegExp(o),a=null;null!==(a=c.exec(e));)if(a[0].trim())if(a[1]){var s=a[1].trim(),u=[s,""];s.indexOf("=")>-1&&(u=s.split("=")),t.attrs[u[0]]=u[1],c.lastIndex--}else a[2]&&(t.attrs[a[2]]=a[3].trim().substring(1,a[3].length-1));return t}var a=/<[a-zA-Z0-9\-\!\/](?:"[^"]*"|'[^']*'|[^'">])*>/g,s=/^\s*$/,u=Object.create(null);function l(e,t){switch(t.type){case"text":return e+t.content;case"tag":return e+="<"+t.name+(t.attrs?function(e){var t=[];for(var n in e)t.push(n+'="'+e[n]+'"');return t.length?" "+t.join(" "):""}(t.attrs):"")+(t.voidElement?"/>":">"),t.voidElement?e:e+t.children.reduce(l,"")+"</"+t.name+">";case"comment":return e+"\x3c!--"+t.comment+"--\x3e"}}var p={parse:function(e,t){t||(t={}),t.components||(t.components=u);var n,r=[],i=[],o=-1,l=!1;if(0!==e.indexOf("<")){var p=e.indexOf("<");r.push({type:"text",content:-1===p?e:e.substring(0,p)})}return e.replace(a,(function(a,u){if(l){if(a!=="</"+n.name+">")return;l=!1}var p,f="/"!==a.charAt(1),d=a.startsWith("\x3c!--"),m=u+a.length,h=e.charAt(m);if(d){var v=c(a);return o<0?(r.push(v),r):((p=i[o]).children.push(v),r)}if(f&&(o++,"tag"===(n=c(a)).type&&t.components[n.name]&&(n.type="component",l=!0),n.voidElement||l||!h||"<"===h||n.children.push({type:"text",content:e.slice(m,e.indexOf("<",m))}),0===o&&r.push(n),(p=i[o-1])&&p.children.push(n),i[o]=n),(!f||n.voidElement)&&(o>-1&&(n.voidElement||n.name===a.slice(2,-1))&&(o--,n=-1===o?r:i[o]),!l&&"<"!==h&&h)){p=-1===o?r:i[o].children;var b=e.indexOf("<",m),y=e.slice(m,-1===b?void 0:b);s.test(y)&&(y=" "),(b>-1&&o+p.length>=0||" "!==y)&&p.push({type:"text",content:y})}})),r},stringify:function(e){return e.reduce((function(e,t){return e+l("",t)}),"")}};t.a=p}}]);