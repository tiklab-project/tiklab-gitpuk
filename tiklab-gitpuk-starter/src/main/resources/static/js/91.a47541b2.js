(window.webpackJsonp=window.webpackJsonp||[]).push([[91],{1203:function(e,r,t){},735:function(e,r,t){"use strict";var n=t(20);Object.defineProperty(r,"__esModule",{value:!0}),r.links=r.deleteSuccessReturnCurrenPage=void 0;var o=n(t(37));function i(e,r){var t=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);r&&(n=n.filter((function(r){return Object.getOwnPropertyDescriptor(e,r).enumerable}))),t.push.apply(t,n)}return t}function u(e){for(var r=1;r<arguments.length;r++){var t=null!=arguments[r]?arguments[r]:{};r%2?i(Object(t),!0).forEach((function(r){(0,o.default)(e,r,t[r])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(t)):i(Object(t)).forEach((function(r){Object.defineProperty(e,r,Object.getOwnPropertyDescriptor(t,r))}))}return e}r.links=function(e,r){var t=[],n=function(e,o){e&&e.map((function(e){if(e.children){var i=e.children.map((function(t){return u(u({},t),{},{type:e[r]})}));n(i,t)}else o.push(e)}))};return n(e,t),t},r.deleteSuccessReturnCurrenPage=function(e,r,t){return e>=t*r?t:e<=(t-1)*r+1?1===t?1:t-1:t}},852:function(e,r,t){"use strict";var n=t(20);Object.defineProperty(r,"__esModule",{value:!0}),r.default=void 0;var o=n(t(853));r.default=o.default},853:function(e,r,t){"use strict";var n=t(20),o=t(38);Object.defineProperty(r,"__esModule",{value:!0}),r.default=void 0;var i=n(t(187)),u=function(e,r){if(!r&&e&&e.__esModule)return e;if(null===e||"object"!=o(e)&&"function"!=typeof e)return{default:e};var t=f(r);if(t&&t.has(e))return t.get(e);var n={__proto__:null},i=Object.defineProperty&&Object.getOwnPropertyDescriptor;for(var u in e)if("default"!==u&&{}.hasOwnProperty.call(e,u)){var c=i?Object.getOwnPropertyDescriptor(e,u):null;c&&(c.get||c.set)?Object.defineProperty(n,u,c):n[u]=e[u]}return n.default=e,t&&t.set(e,n),n}(t(0)),c=t(78),a=t(735);function f(e){if("function"!=typeof WeakMap)return null;var r=new WeakMap,t=new WeakMap;return(f=function(e){return e?t:r})(e)}r.default=(0,c.inject)("systemRoleStore")((0,c.observer)((function(e){var r=e.expandedTree,t=e.setExpandedTree,n=e.applicationRouters,o=e.outerPath,c=e.pathKey,f=void 0===c?"id":c,s=e.noAccessPath,l=e.systemRoleStore,p=l.systemPermissions,d=l.privilegeLoading,v=e.location.pathname,y=(0,a.links)(n,f);return(0,u.useEffect)((function(){if(v===o)if(p&&p.length>0){var n=y.find((function(e){return p.includes(e.purviewCode)}));if(n)e.history.replace(n[f]);else{var u=y.find((function(e){return!e.purviewCode}));u&&e.history.replace(u[f])}}else{var c=y.find((function(e){return!e.purviewCode}));c&&e.history.replace(c[f])}else{var a=y.find((function(e){return e[f]===v}));if(a&&(r&&t([a.type].concat((0,i.default)(r))),a.purviewCode&&!d&&!p.includes(a.purviewCode)))return e.history.replace(s)}}),[v,p]),e.children})))}}]);