(window.webpackJsonp=window.webpackJsonp||[]).push([[94],{1413:function(t,e,r){"use strict";r.r(e);r(64);var n=r(45),o=(r(144),r(88)),i=(r(86),r(30)),a=r(0),c=r.n(a),u=r(706),l=r(771),s=r(78),f=r(526),h=r(298),m=r(647);function p(t){return(p="function"==typeof Symbol&&"symbol"==typeof Symbol.iterator?function(t){return typeof t}:function(t){return t&&"function"==typeof Symbol&&t.constructor===Symbol&&t!==Symbol.prototype?"symbol":typeof t})(t)}var y="/Users/limingliang/work/work-project/web/tiklab-gitpuk-ui/src/repository/setting/branch/BranchSetting.js";function v(){/*! regenerator-runtime -- Copyright (c) 2014-present, Facebook, Inc. -- license (MIT): https://github.com/facebook/regenerator/blob/main/LICENSE */v=function(){return e};var t,e={},r=Object.prototype,n=r.hasOwnProperty,o=Object.defineProperty||function(t,e,r){t[e]=r.value},i="function"==typeof Symbol?Symbol:{},a=i.iterator||"@@iterator",c=i.asyncIterator||"@@asyncIterator",u=i.toStringTag||"@@toStringTag";function l(t,e,r){return Object.defineProperty(t,e,{value:r,enumerable:!0,configurable:!0,writable:!0}),t[e]}try{l({},"")}catch(t){l=function(t,e,r){return t[e]=r}}function s(t,e,r,n){var i=e&&e.prototype instanceof b?e:b,a=Object.create(i.prototype),c=new B(n||[]);return o(a,"_invoke",{value:k(t,r,c)}),a}function f(t,e,r){try{return{type:"normal",arg:t.call(e,r)}}catch(t){return{type:"throw",arg:t}}}e.wrap=s;var h="suspendedStart",m="executing",y="completed",d={};function b(){}function g(){}function w(){}var N={};l(N,a,(function(){return this}));var _=Object.getPrototypeOf,E=_&&_(_(A([])));E&&E!==r&&n.call(E,a)&&(N=E);var x=w.prototype=b.prototype=Object.create(N);function L(t){["next","throw","return"].forEach((function(e){l(t,e,(function(t){return this._invoke(e,t)}))}))}function j(t,e){function r(o,i,a,c){var u=f(t[o],t,i);if("throw"!==u.type){var l=u.arg,s=l.value;return s&&"object"==p(s)&&n.call(s,"__await")?e.resolve(s.__await).then((function(t){r("next",t,a,c)}),(function(t){r("throw",t,a,c)})):e.resolve(s).then((function(t){l.value=t,a(l)}),(function(t){return r("throw",t,a,c)}))}c(u.arg)}var i;o(this,"_invoke",{value:function(t,n){function o(){return new e((function(e,o){r(t,n,e,o)}))}return i=i?i.then(o,o):o()}})}function k(e,r,n){var o=h;return function(i,a){if(o===m)throw Error("Generator is already running");if(o===y){if("throw"===i)throw a;return{value:t,done:!0}}for(n.method=i,n.arg=a;;){var c=n.delegate;if(c){var u=O(c,n);if(u){if(u===d)continue;return u}}if("next"===n.method)n.sent=n._sent=n.arg;else if("throw"===n.method){if(o===h)throw o=y,n.arg;n.dispatchException(n.arg)}else"return"===n.method&&n.abrupt("return",n.arg);o=m;var l=f(e,r,n);if("normal"===l.type){if(o=n.done?y:"suspendedYield",l.arg===d)continue;return{value:l.arg,done:n.done}}"throw"===l.type&&(o=y,n.method="throw",n.arg=l.arg)}}}function O(e,r){var n=r.method,o=e.iterator[n];if(o===t)return r.delegate=null,"throw"===n&&e.iterator.return&&(r.method="return",r.arg=t,O(e,r),"throw"===r.method)||"return"!==n&&(r.method="throw",r.arg=new TypeError("The iterator does not provide a '"+n+"' method")),d;var i=f(o,e.iterator,r.arg);if("throw"===i.type)return r.method="throw",r.arg=i.arg,r.delegate=null,d;var a=i.arg;return a?a.done?(r[e.resultName]=a.value,r.next=e.nextLoc,"return"!==r.method&&(r.method="next",r.arg=t),r.delegate=null,d):a:(r.method="throw",r.arg=new TypeError("iterator result is not an object"),r.delegate=null,d)}function S(t){var e={tryLoc:t[0]};1 in t&&(e.catchLoc=t[1]),2 in t&&(e.finallyLoc=t[2],e.afterLoc=t[3]),this.tryEntries.push(e)}function P(t){var e=t.completion||{};e.type="normal",delete e.arg,t.completion=e}function B(t){this.tryEntries=[{tryLoc:"root"}],t.forEach(S,this),this.reset(!0)}function A(e){if(e||""===e){var r=e[a];if(r)return r.call(e);if("function"==typeof e.next)return e;if(!isNaN(e.length)){var o=-1,i=function r(){for(;++o<e.length;)if(n.call(e,o))return r.value=e[o],r.done=!1,r;return r.value=t,r.done=!0,r};return i.next=i}}throw new TypeError(p(e)+" is not iterable")}return g.prototype=w,o(x,"constructor",{value:w,configurable:!0}),o(w,"constructor",{value:g,configurable:!0}),g.displayName=l(w,u,"GeneratorFunction"),e.isGeneratorFunction=function(t){var e="function"==typeof t&&t.constructor;return!!e&&(e===g||"GeneratorFunction"===(e.displayName||e.name))},e.mark=function(t){return Object.setPrototypeOf?Object.setPrototypeOf(t,w):(t.__proto__=w,l(t,u,"GeneratorFunction")),t.prototype=Object.create(x),t},e.awrap=function(t){return{__await:t}},L(j.prototype),l(j.prototype,c,(function(){return this})),e.AsyncIterator=j,e.async=function(t,r,n,o,i){void 0===i&&(i=Promise);var a=new j(s(t,r,n,o),i);return e.isGeneratorFunction(r)?a:a.next().then((function(t){return t.done?t.value:a.next()}))},L(x),l(x,u,"Generator"),l(x,a,(function(){return this})),l(x,"toString",(function(){return"[object Generator]"})),e.keys=function(t){var e=Object(t),r=[];for(var n in e)r.push(n);return r.reverse(),function t(){for(;r.length;){var n=r.pop();if(n in e)return t.value=n,t.done=!1,t}return t.done=!0,t}},e.values=A,B.prototype={constructor:B,reset:function(e){if(this.prev=0,this.next=0,this.sent=this._sent=t,this.done=!1,this.delegate=null,this.method="next",this.arg=t,this.tryEntries.forEach(P),!e)for(var r in this)"t"===r.charAt(0)&&n.call(this,r)&&!isNaN(+r.slice(1))&&(this[r]=t)},stop:function(){this.done=!0;var t=this.tryEntries[0].completion;if("throw"===t.type)throw t.arg;return this.rval},dispatchException:function(e){if(this.done)throw e;var r=this;function o(n,o){return c.type="throw",c.arg=e,r.next=n,o&&(r.method="next",r.arg=t),!!o}for(var i=this.tryEntries.length-1;i>=0;--i){var a=this.tryEntries[i],c=a.completion;if("root"===a.tryLoc)return o("end");if(a.tryLoc<=this.prev){var u=n.call(a,"catchLoc"),l=n.call(a,"finallyLoc");if(u&&l){if(this.prev<a.catchLoc)return o(a.catchLoc,!0);if(this.prev<a.finallyLoc)return o(a.finallyLoc)}else if(u){if(this.prev<a.catchLoc)return o(a.catchLoc,!0)}else{if(!l)throw Error("try statement without catch or finally");if(this.prev<a.finallyLoc)return o(a.finallyLoc)}}}},abrupt:function(t,e){for(var r=this.tryEntries.length-1;r>=0;--r){var o=this.tryEntries[r];if(o.tryLoc<=this.prev&&n.call(o,"finallyLoc")&&this.prev<o.finallyLoc){var i=o;break}}i&&("break"===t||"continue"===t)&&i.tryLoc<=e&&e<=i.finallyLoc&&(i=null);var a=i?i.completion:{};return a.type=t,a.arg=e,i?(this.method="next",this.next=i.finallyLoc,d):this.complete(a)},complete:function(t,e){if("throw"===t.type)throw t.arg;return"break"===t.type||"continue"===t.type?this.next=t.arg:"return"===t.type?(this.rval=this.arg=t.arg,this.method="return",this.next="end"):"normal"===t.type&&e&&(this.next=e),d},finish:function(t){for(var e=this.tryEntries.length-1;e>=0;--e){var r=this.tryEntries[e];if(r.finallyLoc===t)return this.complete(r.completion,r.afterLoc),P(r),d}},catch:function(t){for(var e=this.tryEntries.length-1;e>=0;--e){var r=this.tryEntries[e];if(r.tryLoc===t){var n=r.completion;if("throw"===n.type){var o=n.arg;P(r)}return o}}throw Error("illegal catch attempt")},delegateYield:function(e,r,n){return this.delegate={iterator:A(e),resultName:r,nextLoc:n},"next"===this.method&&(this.arg=t),d}},e}function d(t,e,r,n,o,i,a){try{var c=t[i](a),u=c.value}catch(t){return void r(t)}c.done?e(u):Promise.resolve(u).then(n,o)}function b(t,e){return function(t){if(Array.isArray(t))return t}(t)||function(t,e){var r=null==t?null:"undefined"!=typeof Symbol&&t[Symbol.iterator]||t["@@iterator"];if(null!=r){var n,o,i,a,c=[],u=!0,l=!1;try{if(i=(r=r.call(t)).next,0===e){if(Object(r)!==r)return;u=!1}else for(;!(u=(n=i.call(r)).done)&&(c.push(n.value),c.length!==e);u=!0);}catch(t){l=!0,o=t}finally{try{if(!u&&null!=r.return&&(a=r.return(),Object(a)!==a))return}finally{if(l)throw o}}return c}}(t,e)||function(t,e){if(t){if("string"==typeof t)return g(t,e);var r={}.toString.call(t).slice(8,-1);return"Object"===r&&t.constructor&&(r=t.constructor.name),"Map"===r||"Set"===r?Array.from(t):"Arguments"===r||/^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(r)?g(t,e):void 0}}(t,e)||function(){throw new TypeError("Invalid attempt to destructure non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method.")}()}function g(t,e){(null==e||e>t.length)&&(e=t.length);for(var r=0,n=Array(e);r<e;r++)n[r]=t[r];return n}e.default=Object(s.inject)("repositoryStore")(Object(s.observer)((function(t){var e=t.repositoryStore,r=t.match,s=e.repositoryInfo,p=e.findRepository,g=l.a.findAllBranch,w=l.a.branchList,N=l.a.updateDefaultBranch,_=(r.params.branch,b(Object(a.useState)(),2)),E=_[0],x=_[1],L=b(Object(a.useState)([]),2),j=L[0],k=L[1];Object(a.useEffect)((function(){g(s.rpyId).then((function(t){0===t.code&&t.data.length>0&&x(s.defaultBranch)}))}),[s.name]);var O=function(){var t,e=(t=v().mark((function t(){return v().wrap((function(t){for(;;)switch(t.prev=t.next){case 0:N({name:E,rpyId:s.rpyId}).then((function(t){0===t.code&&p(s.rpyId)}));case 1:case"end":return t.stop()}}),t)})),function(){var e=this,r=arguments;return new Promise((function(n,o){var i=t.apply(e,r);function a(t){d(i,n,o,a,c,"next",t)}function c(t){d(i,n,o,a,c,"throw",t)}a(void 0)}))});return function(){return e.apply(this,arguments)}}(),S=function(t){return j.some((function(e){return e===t}))},P=function(t,e){return c.a.createElement("div",{key:t.key,className:"".concat(e>0?" border-top":""),__source:{fileName:y,lineNumber:123,columnNumber:16}},c.a.createElement("div",{className:"branch-setting-li-top ".concat(S(t.key)?"branch-setting-li-select":""),onClick:function(){return e=t.key,void(S(e)?k(j.filter((function(t){return t!==e}))):k(j.concat(e)));var e},__source:{fileName:y,lineNumber:124,columnNumber:13}},c.a.createElement("div",{className:"branch-setting-li-icon",__source:{fileName:y,lineNumber:126,columnNumber:17}},t.icon),c.a.createElement("div",{className:"branch-setting-li-center",__source:{fileName:y,lineNumber:127,columnNumber:17}},c.a.createElement("div",{className:"branch-setting-li-title",__source:{fileName:y,lineNumber:128,columnNumber:21}},t.title),!S(t.key)&&c.a.createElement("div",{className:"branch-setting-li-desc",__source:{fileName:y,lineNumber:131,columnNumber:25}},t.desc)),c.a.createElement("div",{className:"branch-setting-li-down",__source:{fileName:y,lineNumber:134,columnNumber:17}},S(t.key)?c.a.createElement(f.default,{__source:{fileName:y,lineNumber:135,columnNumber:48}}):c.a.createElement(h.default,{__source:{fileName:y,lineNumber:135,columnNumber:65}}))),c.a.createElement("div",{className:"".concat(S(t.key)?"branch-setting-li-bottom":"branch-setting-li-none"),__source:{fileName:y,lineNumber:138,columnNumber:13}},S(t.key)&&t.content))},B=[{key:1,title:"切换分支",desc:"切换分支",icon:c.a.createElement(m.a,{style:{fontSize:16},__source:{fileName:y,lineNumber:153,columnNumber:21}}),enCode:"house_update",content:c.a.createElement("div",{className:"branch-setting-mode",__source:{fileName:y,lineNumber:155,columnNumber:22}},c.a.createElement("div",{className:"mode-title",__source:{fileName:y,lineNumber:156,columnNumber:21}},"默认分支"),c.a.createElement("div",{className:"mode-desc",__source:{fileName:y,lineNumber:157,columnNumber:21}},"默认分支被视为代码库中的基本分支，是所有 clone、代码提交和合并请求的目标分支"),c.a.createElement("div",{__source:{fileName:y,lineNumber:158,columnNumber:21}},c.a.createElement(i.default,{style:{width:200},defaultValue:E,allowClear:!0,onChange:function(t){x(t)},placeholder:"分支",__source:{fileName:y,lineNumber:159,columnNumber:25}},w.length&&w.map((function(t){return c.a.createElement(i.default.Option,{key:t.branchName,value:t.branchName,__source:{fileName:y,lineNumber:163,columnNumber:45}},t.branchName)}))),c.a.createElement("div",{style:{marginTop:20},__source:{fileName:y,lineNumber:169,columnNumber:25}},E===s.defaultBranch?c.a.createElement(o.default,{type:"primary",disabled:!0,__source:{fileName:y,lineNumber:172,columnNumber:37}},"更新"):c.a.createElement(o.default,{type:"primary",onClick:O,__source:{fileName:y,lineNumber:173,columnNumber:37}},"更新"))))}];return c.a.createElement("div",{className:"drop-down  xcode  branch-setting",__source:{fileName:y,lineNumber:227,columnNumber:9}},c.a.createElement(n.default,{sm:{span:"24"},md:{span:"24"},lg:{span:"24"},xl:{span:"20",offset:"2"},xxl:{span:"18",offset:"3"},__source:{fileName:y,lineNumber:228,columnNumber:13}},c.a.createElement(u.a,{firstItem:"BranchSetting",__source:{fileName:y,lineNumber:235,columnNumber:21}}),c.a.createElement("div",{className:"border-margin",__source:{fileName:y,lineNumber:236,columnNumber:21}},c.a.createElement("div",{className:"branch-setting-li",__source:{fileName:y,lineNumber:237,columnNumber:25}},B.map((function(t,e){return P(t,e)}))))))})))},771:function(t,e,r){"use strict";r(54);var n,o,i,a,c,u,l,s,f=r(22),h=r(8),m=r(6);function p(t){return(p="function"==typeof Symbol&&"symbol"==typeof Symbol.iterator?function(t){return typeof t}:function(t){return t&&"function"==typeof Symbol&&t.constructor===Symbol&&t!==Symbol.prototype?"symbol":typeof t})(t)}function y(){/*! regenerator-runtime -- Copyright (c) 2014-present, Facebook, Inc. -- license (MIT): https://github.com/facebook/regenerator/blob/main/LICENSE */y=function(){return e};var t,e={},r=Object.prototype,n=r.hasOwnProperty,o=Object.defineProperty||function(t,e,r){t[e]=r.value},i="function"==typeof Symbol?Symbol:{},a=i.iterator||"@@iterator",c=i.asyncIterator||"@@asyncIterator",u=i.toStringTag||"@@toStringTag";function l(t,e,r){return Object.defineProperty(t,e,{value:r,enumerable:!0,configurable:!0,writable:!0}),t[e]}try{l({},"")}catch(t){l=function(t,e,r){return t[e]=r}}function s(t,e,r,n){var i=e&&e.prototype instanceof b?e:b,a=Object.create(i.prototype),c=new B(n||[]);return o(a,"_invoke",{value:k(t,r,c)}),a}function f(t,e,r){try{return{type:"normal",arg:t.call(e,r)}}catch(t){return{type:"throw",arg:t}}}e.wrap=s;var h="suspendedStart",m="executing",v="completed",d={};function b(){}function g(){}function w(){}var N={};l(N,a,(function(){return this}));var _=Object.getPrototypeOf,E=_&&_(_(A([])));E&&E!==r&&n.call(E,a)&&(N=E);var x=w.prototype=b.prototype=Object.create(N);function L(t){["next","throw","return"].forEach((function(e){l(t,e,(function(t){return this._invoke(e,t)}))}))}function j(t,e){function r(o,i,a,c){var u=f(t[o],t,i);if("throw"!==u.type){var l=u.arg,s=l.value;return s&&"object"==p(s)&&n.call(s,"__await")?e.resolve(s.__await).then((function(t){r("next",t,a,c)}),(function(t){r("throw",t,a,c)})):e.resolve(s).then((function(t){l.value=t,a(l)}),(function(t){return r("throw",t,a,c)}))}c(u.arg)}var i;o(this,"_invoke",{value:function(t,n){function o(){return new e((function(e,o){r(t,n,e,o)}))}return i=i?i.then(o,o):o()}})}function k(e,r,n){var o=h;return function(i,a){if(o===m)throw Error("Generator is already running");if(o===v){if("throw"===i)throw a;return{value:t,done:!0}}for(n.method=i,n.arg=a;;){var c=n.delegate;if(c){var u=O(c,n);if(u){if(u===d)continue;return u}}if("next"===n.method)n.sent=n._sent=n.arg;else if("throw"===n.method){if(o===h)throw o=v,n.arg;n.dispatchException(n.arg)}else"return"===n.method&&n.abrupt("return",n.arg);o=m;var l=f(e,r,n);if("normal"===l.type){if(o=n.done?v:"suspendedYield",l.arg===d)continue;return{value:l.arg,done:n.done}}"throw"===l.type&&(o=v,n.method="throw",n.arg=l.arg)}}}function O(e,r){var n=r.method,o=e.iterator[n];if(o===t)return r.delegate=null,"throw"===n&&e.iterator.return&&(r.method="return",r.arg=t,O(e,r),"throw"===r.method)||"return"!==n&&(r.method="throw",r.arg=new TypeError("The iterator does not provide a '"+n+"' method")),d;var i=f(o,e.iterator,r.arg);if("throw"===i.type)return r.method="throw",r.arg=i.arg,r.delegate=null,d;var a=i.arg;return a?a.done?(r[e.resultName]=a.value,r.next=e.nextLoc,"return"!==r.method&&(r.method="next",r.arg=t),r.delegate=null,d):a:(r.method="throw",r.arg=new TypeError("iterator result is not an object"),r.delegate=null,d)}function S(t){var e={tryLoc:t[0]};1 in t&&(e.catchLoc=t[1]),2 in t&&(e.finallyLoc=t[2],e.afterLoc=t[3]),this.tryEntries.push(e)}function P(t){var e=t.completion||{};e.type="normal",delete e.arg,t.completion=e}function B(t){this.tryEntries=[{tryLoc:"root"}],t.forEach(S,this),this.reset(!0)}function A(e){if(e||""===e){var r=e[a];if(r)return r.call(e);if("function"==typeof e.next)return e;if(!isNaN(e.length)){var o=-1,i=function r(){for(;++o<e.length;)if(n.call(e,o))return r.value=e[o],r.done=!1,r;return r.value=t,r.done=!0,r};return i.next=i}}throw new TypeError(p(e)+" is not iterable")}return g.prototype=w,o(x,"constructor",{value:w,configurable:!0}),o(w,"constructor",{value:g,configurable:!0}),g.displayName=l(w,u,"GeneratorFunction"),e.isGeneratorFunction=function(t){var e="function"==typeof t&&t.constructor;return!!e&&(e===g||"GeneratorFunction"===(e.displayName||e.name))},e.mark=function(t){return Object.setPrototypeOf?Object.setPrototypeOf(t,w):(t.__proto__=w,l(t,u,"GeneratorFunction")),t.prototype=Object.create(x),t},e.awrap=function(t){return{__await:t}},L(j.prototype),l(j.prototype,c,(function(){return this})),e.AsyncIterator=j,e.async=function(t,r,n,o,i){void 0===i&&(i=Promise);var a=new j(s(t,r,n,o),i);return e.isGeneratorFunction(r)?a:a.next().then((function(t){return t.done?t.value:a.next()}))},L(x),l(x,u,"Generator"),l(x,a,(function(){return this})),l(x,"toString",(function(){return"[object Generator]"})),e.keys=function(t){var e=Object(t),r=[];for(var n in e)r.push(n);return r.reverse(),function t(){for(;r.length;){var n=r.pop();if(n in e)return t.value=n,t.done=!1,t}return t.done=!0,t}},e.values=A,B.prototype={constructor:B,reset:function(e){if(this.prev=0,this.next=0,this.sent=this._sent=t,this.done=!1,this.delegate=null,this.method="next",this.arg=t,this.tryEntries.forEach(P),!e)for(var r in this)"t"===r.charAt(0)&&n.call(this,r)&&!isNaN(+r.slice(1))&&(this[r]=t)},stop:function(){this.done=!0;var t=this.tryEntries[0].completion;if("throw"===t.type)throw t.arg;return this.rval},dispatchException:function(e){if(this.done)throw e;var r=this;function o(n,o){return c.type="throw",c.arg=e,r.next=n,o&&(r.method="next",r.arg=t),!!o}for(var i=this.tryEntries.length-1;i>=0;--i){var a=this.tryEntries[i],c=a.completion;if("root"===a.tryLoc)return o("end");if(a.tryLoc<=this.prev){var u=n.call(a,"catchLoc"),l=n.call(a,"finallyLoc");if(u&&l){if(this.prev<a.catchLoc)return o(a.catchLoc,!0);if(this.prev<a.finallyLoc)return o(a.finallyLoc)}else if(u){if(this.prev<a.catchLoc)return o(a.catchLoc,!0)}else{if(!l)throw Error("try statement without catch or finally");if(this.prev<a.finallyLoc)return o(a.finallyLoc)}}}},abrupt:function(t,e){for(var r=this.tryEntries.length-1;r>=0;--r){var o=this.tryEntries[r];if(o.tryLoc<=this.prev&&n.call(o,"finallyLoc")&&this.prev<o.finallyLoc){var i=o;break}}i&&("break"===t||"continue"===t)&&i.tryLoc<=e&&e<=i.finallyLoc&&(i=null);var a=i?i.completion:{};return a.type=t,a.arg=e,i?(this.method="next",this.next=i.finallyLoc,d):this.complete(a)},complete:function(t,e){if("throw"===t.type)throw t.arg;return"break"===t.type||"continue"===t.type?this.next=t.arg:"return"===t.type?(this.rval=this.arg=t.arg,this.method="return",this.next="end"):"normal"===t.type&&e&&(this.next=e),d},finish:function(t){for(var e=this.tryEntries.length-1;e>=0;--e){var r=this.tryEntries[e];if(r.finallyLoc===t)return this.complete(r.completion,r.afterLoc),P(r),d}},catch:function(t){for(var e=this.tryEntries.length-1;e>=0;--e){var r=this.tryEntries[e];if(r.tryLoc===t){var n=r.completion;if("throw"===n.type){var o=n.arg;P(r)}return o}}throw Error("illegal catch attempt")},delegateYield:function(e,r,n){return this.delegate={iterator:A(e),resultName:r,nextLoc:n},"next"===this.method&&(this.arg=t),d}},e}function v(t,e,r,n,o,i,a){try{var c=t[i](a),u=c.value}catch(t){return void r(t)}c.done?e(u):Promise.resolve(u).then(n,o)}function d(t){return function(){var e=this,r=arguments;return new Promise((function(n,o){var i=t.apply(e,r);function a(t){v(i,n,o,a,c,"next",t)}function c(t){v(i,n,o,a,c,"throw",t)}a(void 0)}))}}function b(t,e,r,n){r&&Object.defineProperty(t,e,{enumerable:r.enumerable,configurable:r.configurable,writable:r.writable,value:r.initializer?r.initializer.call(n):void 0})}function g(t,e){for(var r=0;r<e.length;r++){var n=e[r];n.enumerable=n.enumerable||!1,n.configurable=!0,"value"in n&&(n.writable=!0),Object.defineProperty(t,N(n.key),n)}}function w(t,e,r){return e&&g(t.prototype,e),r&&g(t,r),Object.defineProperty(t,"prototype",{writable:!1}),t}function N(t){var e=function(t,e){if("object"!=p(t)||!t)return t;var r=t[Symbol.toPrimitive];if(void 0!==r){var n=r.call(t,e||"default");if("object"!=p(n))return n;throw new TypeError("@@toPrimitive must return a primitive value.")}return("string"===e?String:Number)(t)}(t,"string");return"symbol"==p(e)?e:e+""}function _(t,e,r,n,o){var i={};return Object.keys(n).forEach((function(t){i[t]=n[t]})),i.enumerable=!!i.enumerable,i.configurable=!!i.configurable,("value"in i||i.initializer)&&(i.writable=!0),i=r.slice().reverse().reduce((function(r,n){return n(t,e,r)||r}),i),o&&void 0!==i.initializer&&(i.value=i.initializer?i.initializer.call(o):void 0,i.initializer=void 0),void 0===i.initializer?(Object.defineProperty(t,e,i),null):i}var E=new(o=_((n=w((function t(){!function(t,e){if(!(t instanceof e))throw new TypeError("Cannot call a class as a function")}(this,t),b(this,"branchList",o,this),b(this,"fresh",i,this),b(this,"findAllBranch",a,this),b(this,"findBranchList",c,this),b(this,"createBranch",u,this),b(this,"deleteBranch",l,this),b(this,"updateDefaultBranch",s,this)}))).prototype,"branchList",[h.observable],{configurable:!0,enumerable:!0,writable:!0,initializer:function(){return[]}}),i=_(n.prototype,"fresh",[h.observable],{configurable:!0,enumerable:!0,writable:!0,initializer:function(){return!1}}),a=_(n.prototype,"findAllBranch",[h.action],{configurable:!0,enumerable:!0,writable:!0,initializer:function(){var t=this;return function(){var e=d(y().mark((function e(r){var n,o;return y().wrap((function(e){for(;;)switch(e.prev=e.next){case 0:return(n=new FormData).append("rpyId",r),e.next=4,m.Axios.post("/branch/findAllBranch",n);case 4:return 0===(o=e.sent).code&&(t.branchList=o.data&&o.data),e.abrupt("return",o);case 7:case"end":return e.stop()}}),e)})));return function(t){return e.apply(this,arguments)}}()}}),c=_(n.prototype,"findBranchList",[h.action],{configurable:!0,enumerable:!0,writable:!0,initializer:function(){var t=this;return function(){var e=d(y().mark((function e(r){var n;return y().wrap((function(e){for(;;)switch(e.prev=e.next){case 0:return e.next=2,m.Axios.post("/branch/findBranchList",r);case 2:return 0===(n=e.sent).code&&(t.branchList=n.data&&n.data),e.abrupt("return",n);case 5:case"end":return e.stop()}}),e)})));return function(t){return e.apply(this,arguments)}}()}}),u=_(n.prototype,"createBranch",[h.action],{configurable:!0,enumerable:!0,writable:!0,initializer:function(){var t=this;return function(){var e=d(y().mark((function e(r){var n;return y().wrap((function(e){for(;;)switch(e.prev=e.next){case 0:return e.next=2,m.Axios.post("/branch/createBranch",r);case 2:return 0===(n=e.sent).code&&(f.default.info("创建成功",.5),t.fresh=!t.fresh),e.abrupt("return",n);case 5:case"end":return e.stop()}}),e)})));return function(t){return e.apply(this,arguments)}}()}}),l=_(n.prototype,"deleteBranch",[h.action],{configurable:!0,enumerable:!0,writable:!0,initializer:function(){var t=this;return function(){var e=d(y().mark((function e(r){return y().wrap((function(e){for(;;)switch(e.prev=e.next){case 0:return e.next=2,m.Axios.post("/branch/deleteBranch",r);case 2:0===e.sent.code&&(f.default.info("删除成功",.5),t.fresh=!t.fresh);case 4:case"end":return e.stop()}}),e)})));return function(t){return e.apply(this,arguments)}}()}}),s=_(n.prototype,"updateDefaultBranch",[h.action],{configurable:!0,enumerable:!0,writable:!0,initializer:function(){return function(){var t=d(y().mark((function t(e){var r;return y().wrap((function(t){for(;;)switch(t.prev=t.next){case 0:return t.next=2,m.Axios.post("/branch/updateDefaultBranch",e);case 2:return 0===(r=t.sent).code?f.default.success("切换成功",1):f.default.error("切换失败",1),t.abrupt("return",r);case 5:case"end":return t.stop()}}),t)})));return function(e){return t.apply(this,arguments)}}()}}),n);e.a=E}}]);