webpackJsonp([2],{

/***/ "./resources/assets/js/admin-lte.js":
/***/ (function(module, exports, __webpack_require__) {

/* WEBPACK VAR INJECTION */(function(__webpack_provided_window_dot_jQuery, __webpack_provided_window_dot_$, $) {
window._ = __webpack_require__("./node_modules/lodash/lodash.js");

/**
 * We'll load jQuery and the Bootstrap jQuery plugin which provides support
 * for JavaScript based Bootstrap features such as modals and tabs. This
 * code may be modified to fit the specific needs of your application.
 */

try {
  __webpack_provided_window_dot_$ = __webpack_provided_window_dot_jQuery = __webpack_require__("./node_modules/jquery/dist/jquery.js");

  __webpack_require__("./node_modules/bootstrap-sass/assets/javascripts/bootstrap.js");

  __webpack_require__("./node_modules/jquery-slimscroll/jquery.slimscroll.js");

  __webpack_require__("./node_modules/fastclick/lib/fastclick.js");

  __webpack_require__("./node_modules/icheck/icheck.js");

  __webpack_require__("./node_modules/admin-lte/dist/js/adminlte.min.js");
} catch (e) {}

/**
 * We'll load the axios HTTP library which allows us to easily issue requests
 * to our Laravel back-end. This library automatically handles sending the
 * CSRF token as a header based on the value of the "XSRF" token cookie.
 */

window.axios = __webpack_require__("./node_modules/axios/index.js");

window.axios.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

/**
 * Next we will register the CSRF Token as a common header with Axios so that
 * all outgoing HTTP requests automatically have it attached. This is just
 * a simple convenience so we don't have to attach every token manually.
 */

var token = document.head.querySelector('meta[name="csrf-token"]');

if (token) {
  window.axios.defaults.headers.common['X-CSRF-TOKEN'] = token.content;
} else {
  console.error('CSRF token not found: https://laravel.com/docs/csrf#csrf-x-csrf-token');
}

/**
 * Echo exposes an expressive API for subscribing to channels and listening
 * for events that are broadcast by Laravel. Echo and event broadcasting
 * allows your team to easily build robust real-time web applications.
 */

// import Echo from 'laravel-echo'

// window.Pusher = require('pusher-js');

// window.Echo = new Echo({
//     broadcaster: 'pusher',
//     key: 'your-pusher-key'
// });

$(document).ready(function () {
  $('.sidebar-menu').tree();
});
/* WEBPACK VAR INJECTION */}.call(exports, __webpack_require__("./node_modules/jquery/dist/jquery.js"), __webpack_require__("./node_modules/jquery/dist/jquery.js"), __webpack_require__("./node_modules/jquery/dist/jquery.js")))

/***/ }),

/***/ "./resources/assets/js/auth.js":
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/* WEBPACK VAR INJECTION */(function($) {

__webpack_require__("./resources/assets/js/admin-lte.js");

function getHashFromUrl(url) {
  return $("<a />").attr("href", url)[0].hash.replace(/^#/, "");
}
$(document).ready(function () {
  $('input').iCheck({
    checkboxClass: 'icheckbox_square-grey',
    radioClass: 'iradio_square-grey'
  });
  //language
  if (typeof Storage === "undefined") {
    // Code for localStorage/sessionStorage.
    //alert('localStorage ready!');
    //} else {
    alert('Sorry! No Web Storage support');
  }

  //do translation
  // function tl(){
  //   var activeLang = localStorage.getItem('active-lang');
  //   if ($('.tl-placeholder').length > 0){
  //     $('.tl-placeholder').each(function(i, n){
  //       var $el = $(this);
  //       var keyLang = typeof $el.attr('tl') !== 'undefined' ? $el.attr('tl') : 'not-found';
  //       $el.attr('placeholder', trans[keyLang][activeLang]);
  //     });
  //   }

  //   if ($('.tl-label').length > 0) {
  //     $('.tl-label').each(function (i, n) {
  //       var $el = $(this);
  //       var keyLang = typeof $el.attr('tl') !== 'undefined' ? $el.attr('tl') : 'not-found';
  //       $el.html(trans[keyLang][activeLang]);
  //     });
  //   }
  // }

  /*function tl() {
    var activeLang = localStorage.getItem('active-lang');
    if ($('.tl-placeholder').length > 0) {
      $('.tl-placeholder').each(function (i, n) {
        var $el = $(this);
        var keyLang = typeof $el.attr('tl') !== 'undefined' ? $el.attr('tl') : 'not-found';
        if (typeof $el.attr('tl') !== 'undefined' && typeof trans[keyLang] !== 'undefined') {
          $el.attr('placeholder', trans[keyLang][activeLang]);
        } else {
          console.log('Translation "' + keyLang + '" not found.');
        }
      });
    }

    if ($('.tl-label').length > 0) {
      $('.tl-label').each(function (i, n) {
        var $el = $(this);
        var keyLang = typeof $el.attr('tl') !== 'undefined' ? $el.attr('tl') : 'not-found';
        if (typeof $el.attr('tl') !== 'undefined' && typeof trans[keyLang] !== 'undefined') {
          $el.html(trans[keyLang][activeLang]);
        } else {
          console.log('Translation "' + keyLang + '" not found.');
        }
      });
    }
  }
  //localStorage.setItem('active-lang', 'en');//default lang
  function setActiveLang(activeLang) {
    $('.lang-options a').removeClass('disabled');
    $('.lang-options a[href="#' + activeLang + '"]').addClass('disabled');
    tl();
  }

  var activeLang = localStorage.getItem('active-lang');
  if (activeLang == null) localStorage.setItem('active-lang', 'en');
  activeLang = localStorage.getItem('active-lang');
  setActiveLang(activeLang);
  //$('.lang-options a[href="#' + activeLang + '"]').addClass('disabled');

  $('.lang-options a').on('click', function (e) {
    e.preventDefault();
    var href = $(this).attr('href');
    activeLang = getHashFromUrl(href);
    localStorage.setItem('active-lang', activeLang);
    setActiveLang(activeLang);
    //console.log(localStorage.getItem('active-lang'));
    //do translation
  });*/
  //end language
});
/* WEBPACK VAR INJECTION */}.call(exports, __webpack_require__("./node_modules/jquery/dist/jquery.js")))

/***/ }),

/***/ 1:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("./resources/assets/js/auth.js");


/***/ })

},[1]);