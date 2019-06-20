webpackJsonp(["support.module"],{

/***/ "../../../../../src/app/pages/support/done/done.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return DoneComponent; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_http__ = __webpack_require__("../../../http/@angular/http.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__core_utils_remoteDataSource__ = __webpack_require__("../../../../../src/app/@core/utils/remoteDataSource.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__pages_component__ = __webpack_require__("../../../../../src/app/pages/pages.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_angular2_toaster__ = __webpack_require__("../../../../angular2-toaster/angular2-toaster.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__core_data_smart_table_service__ = __webpack_require__("../../../../../src/app/@core/data/smart-table.service.ts");
var __extends = (this && this.__extends) || (function () {
    var extendStatics = Object.setPrototypeOf ||
        ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
        function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};






var DoneComponent = (function (_super) {
    __extends(DoneComponent, _super);
    function DoneComponent(service, http, toasterService) {
        var _this = _super.call(this, http, toasterService) || this;
        _this.service = service;
        _this.type_filter = [
            { value: '待处理', title: '待处理' },
            { value: '误报', title: '误报' },
            { value: '完成', title: '完成' },
        ];
        _this.settings = {
            add: {
                confirmCreate: true,
                addButtonContent: '<i class="nb-plus"></i>',
                createButtonContent: '<i class="nb-checkmark"></i>',
                cancelButtonContent: '<i class="nb-close"></i>',
                inputClass: "add-input"
            },
            edit: {
                editButtonContent: '<i class="nb-edit"></i>',
                saveButtonContent: '<i class="nb-checkmark"></i>',
                cancelButtonContent: '<i class="nb-close"></i>',
                confirmSave: true
            },
            delete: {
                deleteButtonContent: '<i class="nb-trash"></i>',
                confirmDelete: true,
            },
            noDataMessage: "没有数据",
            columns: {
                id: {
                    title: "ID",
                    type: "number",
                    addable: false,
                    editable: false,
                },
                eui: {
                    title: '设备号',
                    type: 'string',
                },
                cause: {
                    title: '故障原因',
                    type: 'string',
                },
                ctime: {
                    title: '故障时间',
                    type: 'string',
                    valuePrepareFunction: function (date) {
                        var d = new Date(date);
                        return d.toLocaleString('zu-ZA').slice(0, 10);
                    }
                },
                action: {
                    title: '处理方式',
                },
                // expired: {
                //     title:'超时处理',
                //     type: 'html',
                //     addable: false,
                //     editable: false,
                //     valuePrepareFunction:(cell,row)=>{
                //         if (row.expired){
                //              return `
                //              <a class="btn btn-sm btn-danger">已超时</a>
                //              `
                //         }
                //         else{
                //              return `
                //              <a class="btn btn-sm btn-info">未超时</a>
                //              `
                //         }
                //     },
                // },
                status: {
                    title: '状态',
                    addable: false,
                    editor: {
                        type: "list",
                        config: {
                            selectText: '选择类别',
                            list: _this.type_filter,
                        },
                    },
                    filter: {
                        type: 'list',
                        config: {
                            selectText: '选择类别',
                            list: _this.type_filter,
                        },
                    },
                }
            },
            actions: false
        };
        _this.url = Object(__WEBPACK_IMPORTED_MODULE_3__pages_component__["a" /* ConventULR */])("/api/op/v1");
        _this.source = new __WEBPACK_IMPORTED_MODULE_2__core_utils_remoteDataSource__["b" /* RemoteDataSource */](http, { endPoint: _this.url, filterFieldKey: '#field#' });
        return _this;
    }
    return DoneComponent;
}(__WEBPACK_IMPORTED_MODULE_2__core_utils_remoteDataSource__["a" /* ActionConfirm */]));
DoneComponent = __decorate([
    Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'padding',
        template: "\n    <toaster-container></toaster-container>\n    <nb-card>\n    <nb-card-header>\n      {{head}}\n     \n    </nb-card-header>\n  \n    <nb-card-body>\n    \n      <ng2-smart-table [settings]=\"settings\" \n      [source]=\"source\"\n       (createConfirm)=\"onCreateConfirm($event)\"\n       (deleteConfirm)=\"onDeleteConfirm($event)\"\n       (editConfirm)=\"onEditConfirm($event)\">\n      </ng2-smart-table>\n    </nb-card-body>\n  </nb-card>\n    "
    }),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_5__core_data_smart_table_service__["a" /* SmartTableService */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_5__core_data_smart_table_service__["a" /* SmartTableService */]) === "function" && _a || Object, typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_1__angular_http__["b" /* Http */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__angular_http__["b" /* Http */]) === "function" && _b || Object, typeof (_c = typeof __WEBPACK_IMPORTED_MODULE_4_angular2_toaster__["b" /* ToasterService */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_4_angular2_toaster__["b" /* ToasterService */]) === "function" && _c || Object])
], DoneComponent);

var _a, _b, _c;
//# sourceMappingURL=done.component.js.map

/***/ }),

/***/ "../../../../../src/app/pages/support/padding/padding-component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return PaddingComponent; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_http__ = __webpack_require__("../../../http/@angular/http.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__core_utils_remoteDataSource__ = __webpack_require__("../../../../../src/app/@core/utils/remoteDataSource.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__pages_component__ = __webpack_require__("../../../../../src/app/pages/pages.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_angular2_toaster__ = __webpack_require__("../../../../angular2-toaster/angular2-toaster.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__core_data_smart_table_service__ = __webpack_require__("../../../../../src/app/@core/data/smart-table.service.ts");
var __extends = (this && this.__extends) || (function () {
    var extendStatics = Object.setPrototypeOf ||
        ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
        function (d, b) { for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p]; };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};






var PaddingComponent = (function (_super) {
    __extends(PaddingComponent, _super);
    function PaddingComponent(service, http, toasterService) {
        var _this = _super.call(this, http, toasterService) || this;
        _this.service = service;
        _this.type_filter = [
            { value: '待处理', title: '待处理' },
            { value: '误报', title: '误报' },
            { value: '完成', title: '完成' },
        ];
        _this.settings = {
            add: {
                confirmCreate: true,
                addButtonContent: '<i class="nb-plus"></i>',
                createButtonContent: '<i class="nb-checkmark"></i>',
                cancelButtonContent: '<i class="nb-close"></i>',
                inputClass: "add-input"
            },
            edit: {
                editButtonContent: '<i class="nb-edit"></i>',
                saveButtonContent: '<i class="nb-checkmark"></i>',
                cancelButtonContent: '<i class="nb-close"></i>',
                confirmSave: true
            },
            delete: {
                deleteButtonContent: '<i class="nb-trash"></i>',
                confirmDelete: true,
            },
            noDataMessage: "没有数据",
            columns: {
                id: {
                    title: "ID",
                    type: "number",
                    addable: false,
                    editable: false,
                },
                eui: {
                    title: '设备号',
                    type: 'string',
                },
                cause: {
                    title: '故障原因',
                    type: 'string',
                },
                ctime: {
                    title: '故障时间',
                    type: 'string',
                    valuePrepareFunction: function (date) {
                        var d = new Date(date);
                        return d.toLocaleString('zu-ZA').slice(0, 10);
                    }
                },
                action: {
                    title: '处理方式',
                },
                expired: {
                    title: '超时处理',
                    type: 'html',
                    addable: false,
                    editable: false,
                    valuePrepareFunction: function (cell, row) {
                        if (row.expired) {
                            return "\n                         <a class=\"btn btn-sm btn-danger\">\u5DF2\u8D85\u65F6</a>\n                         ";
                        }
                        else {
                            return "\n                         <a class=\"btn btn-sm btn-info\">\u672A\u8D85\u65F6</a>\n                         ";
                        }
                    },
                },
                status: {
                    title: '状态',
                    addable: false,
                    editor: {
                        type: "list",
                        config: {
                            selectText: '选择类别',
                            list: _this.type_filter,
                        },
                    },
                    filter: {
                        type: 'list',
                        config: {
                            selectText: '选择类别',
                            list: _this.type_filter,
                        },
                    },
                }
            },
            actions: {
                columnTitle: "操作",
                add: false
            }
        };
        _this.url = Object(__WEBPACK_IMPORTED_MODULE_3__pages_component__["a" /* ConventULR */])("/api/op/v1/unsolved");
        _this.source = new __WEBPACK_IMPORTED_MODULE_2__core_utils_remoteDataSource__["b" /* RemoteDataSource */](http, { endPoint: _this.url, filterFieldKey: '#field#' });
        return _this;
    }
    return PaddingComponent;
}(__WEBPACK_IMPORTED_MODULE_2__core_utils_remoteDataSource__["a" /* ActionConfirm */]));
PaddingComponent = __decorate([
    Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'padding',
        template: "\n    <toaster-container></toaster-container>\n    <nb-card>\n    <nb-card-header>\n      {{head}}\n     \n    </nb-card-header>\n  \n    <nb-card-body>\n    \n      <ng2-smart-table [settings]=\"settings\" \n      [source]=\"source\"\n       (createConfirm)=\"onCreateConfirm($event)\"\n       (deleteConfirm)=\"onDeleteConfirm($event)\"\n       (editConfirm)=\"onEditConfirm($event)\">\n      </ng2-smart-table>\n    </nb-card-body>\n  </nb-card>\n    "
    }),
    __metadata("design:paramtypes", [typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_5__core_data_smart_table_service__["a" /* SmartTableService */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_5__core_data_smart_table_service__["a" /* SmartTableService */]) === "function" && _a || Object, typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_1__angular_http__["b" /* Http */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_1__angular_http__["b" /* Http */]) === "function" && _b || Object, typeof (_c = typeof __WEBPACK_IMPORTED_MODULE_4_angular2_toaster__["b" /* ToasterService */] !== "undefined" && __WEBPACK_IMPORTED_MODULE_4_angular2_toaster__["b" /* ToasterService */]) === "function" && _c || Object])
], PaddingComponent);

var _a, _b, _c;
//# sourceMappingURL=padding-component.js.map

/***/ }),

/***/ "../../../../../src/app/pages/support/suport-component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return SupportComponent; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};

var SupportComponent = (function () {
    function SupportComponent() {
    }
    return SupportComponent;
}());
SupportComponent = __decorate([
    Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'ngx-components',
        template: "\n    <router-outlet></router-outlet>\n  ",
    })
], SupportComponent);

//# sourceMappingURL=suport-component.js.map

/***/ }),

/***/ "../../../../../src/app/pages/support/support-routing.module.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return ComponentsRoutingModule; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "b", function() { return routedComponents; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_router__ = __webpack_require__("../../../router/@angular/router.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__suport_component__ = __webpack_require__("../../../../../src/app/pages/support/suport-component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__padding_padding_component__ = __webpack_require__("../../../../../src/app/pages/support/padding/padding-component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__done_done_component__ = __webpack_require__("../../../../../src/app/pages/support/done/done.component.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__welcome_welcome_component__ = __webpack_require__("../../../../../src/app/pages/support/welcome/welcome.component.ts");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};






var routes = [{
        path: '',
        component: __WEBPACK_IMPORTED_MODULE_2__suport_component__["a" /* SupportComponent */],
        children: [
            {
                path: 'padding',
                component: __WEBPACK_IMPORTED_MODULE_3__padding_padding_component__["a" /* PaddingComponent */],
            }, {
                path: 'done',
                component: __WEBPACK_IMPORTED_MODULE_4__done_done_component__["a" /* DoneComponent */],
            },
            {
                path: "welcome",
                component: __WEBPACK_IMPORTED_MODULE_5__welcome_welcome_component__["a" /* WelcomeComponent */]
            }
        ],
    }];
var ComponentsRoutingModule = (function () {
    function ComponentsRoutingModule() {
    }
    return ComponentsRoutingModule;
}());
ComponentsRoutingModule = __decorate([
    Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
        imports: [__WEBPACK_IMPORTED_MODULE_1__angular_router__["d" /* RouterModule */].forChild(routes)],
        exports: [__WEBPACK_IMPORTED_MODULE_1__angular_router__["d" /* RouterModule */]],
    })
], ComponentsRoutingModule);

var routedComponents = [
    __WEBPACK_IMPORTED_MODULE_2__suport_component__["a" /* SupportComponent */],
    __WEBPACK_IMPORTED_MODULE_3__padding_padding_component__["a" /* PaddingComponent */],
    __WEBPACK_IMPORTED_MODULE_4__done_done_component__["a" /* DoneComponent */],
    __WEBPACK_IMPORTED_MODULE_5__welcome_welcome_component__["a" /* WelcomeComponent */]
];
//# sourceMappingURL=support-routing.module.js.map

/***/ }),

/***/ "../../../../../src/app/pages/support/support.module.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "SupportModule", function() { return SupportModule; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_ng2_smart_table__ = __webpack_require__("../../../../ng2-smart-table/index.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__core_data_smart_table_service__ = __webpack_require__("../../../../../src/app/@core/data/smart-table.service.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_ng2_tree__ = __webpack_require__("../../../../ng2-tree/index.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_ng2_tree___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_3_ng2_tree__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_angular2_toaster__ = __webpack_require__("../../../../angular2-toaster/angular2-toaster.js");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__theme_theme_module__ = __webpack_require__("../../../../../src/app/@theme/theme.module.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__support_routing_module__ = __webpack_require__("../../../../../src/app/pages/support/support-routing.module.ts");
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7_ngx_echarts__ = __webpack_require__("../../../../ngx-echarts/index.js");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};








var SupportModule = (function () {
    function SupportModule() {
    }
    return SupportModule;
}());
SupportModule = __decorate([
    Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
        imports: [
            __WEBPACK_IMPORTED_MODULE_5__theme_theme_module__["a" /* ThemeModule */],
            __WEBPACK_IMPORTED_MODULE_6__support_routing_module__["a" /* ComponentsRoutingModule */],
            __WEBPACK_IMPORTED_MODULE_3_ng2_tree__["TreeModule"],
            __WEBPACK_IMPORTED_MODULE_4_angular2_toaster__["a" /* ToasterModule */],
            __WEBPACK_IMPORTED_MODULE_1_ng2_smart_table__["c" /* Ng2SmartTableModule */],
            __WEBPACK_IMPORTED_MODULE_7_ngx_echarts__["a" /* AngularEchartsModule */],
        ],
        declarations: __WEBPACK_IMPORTED_MODULE_6__support_routing_module__["b" /* routedComponents */].slice(),
        providers: [
            __WEBPACK_IMPORTED_MODULE_2__core_data_smart_table_service__["a" /* SmartTableService */],
        ],
    })
], SupportModule);

//# sourceMappingURL=support.module.js.map

/***/ }),

/***/ "../../../../../src/app/pages/support/welcome/welcome.component.ts":
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return WelcomeComponent; });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__("../../../core/@angular/core.es5.js");
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};

var WelcomeComponent = (function () {
    function WelcomeComponent() {
    }
    return WelcomeComponent;
}());
WelcomeComponent = __decorate([
    Object(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
        selector: 'padding',
        template: "\n    <toaster-container></toaster-container>\n    <nb-card>\n    <nb-card-header>\n     \n    </nb-card-header>\n  \n    <nb-card-body>\n    <h1>\u60A8\u597D, \u8BF7\u9009\u62E9\u60A8\u8981\u8FDB\u5165\u7684\u680F\u76EE</h1>\n    </nb-card-body>\n    \n  </nb-card>\n    "
    })
], WelcomeComponent);

//# sourceMappingURL=welcome.component.js.map

/***/ })

});
//# sourceMappingURL=support.module.chunk.js.map