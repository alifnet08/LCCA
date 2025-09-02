webpackJsonp([3],{

/***/ "./resources/assets/js/front.js":
/***/ (function(module, exports, __webpack_require__) {

/* WEBPACK VAR INJECTION */(function($) {function getHashFromUrl(url) {
  return $("<a />").attr("href", url)[0].hash.replace(/^#/, "");
}

function tl() {
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

  /*if ($('.tl-content').length > 0) {
    $('.tl-content').each(function (i, n) {
      var $el = $(this);
      // var lang = typeof $el.attr('lang') !== 'undefined' ? $el.attr('lang') : 'not-found';
      // console.log(lang);
      if (typeof $el.attr('lang') !== 'undefined') {
        $el.addClass('hide');
        //console.log($('.tl-content[lang="' + lang + '"]').text());
        $('[lang="' + activeLang + '"]').removeClass('hide');
      } else {
        console.log('Translation "' + lang + '" not found.');
      }
    });
  }*/
}

/*function setActiveLang(activeLang) {
  $('.lang-options a').removeClass('disabled');
  $('.lang-options a[href="#' + activeLang + '"]').addClass('disabled');
  tl();
}*/
//sama seperti pada auth.js
/*var activeLang = localStorage.getItem('active-lang');
if (activeLang == null) localStorage.setItem('active-lang', 'en');
activeLang = localStorage.getItem('active-lang');
setActiveLang(activeLang);
*/
// function tl2(){
//   var activeLang = localStorage.getItem('active-lang');
//   $.each(trans, function(key, val){
//     replaceTextOnPage('--' + key + '--', val[activeLang]);
//     // var keyword = new RegExp(i, "g");
//     // tpl = tpl.replace(keyword, replace_with);
//   })
// }

// function replaceTextOnPage(from, to) {
//   getAllTextNodes().forEach(function (node) {
//     node.nodeValue = node.nodeValue.replace(new RegExp(quote(from), 'g'), to);
//   });

//   function getAllTextNodes() {
//     var result = [];

//     (function scanSubTree(node) {
//       if (node.childNodes.length)
//         for (var i = 0; i < node.childNodes.length; i++)
//           scanSubTree(node.childNodes[i]);
//       else if (node.nodeType == Node.TEXT_NODE)
//         result.push(node);
//     })(document);

//     return result;
//   }

//   function quote(str) {
//     return (str + '').replace(/([.?*+^$[\]\\(){}|-])/g, "\\$1");
//   }
// }

function reInitSelect2(){
	 if ($('.s2').length > 0) {
	    $('.s2').select2();
	 }
	 
	 if($('input').length > 0){
		 $('input').iCheck({
		    checkboxClass: 'icheckbox_square-grey',
		    radioClass: 'iradio_square-grey'
		 });
	 }
	 
}

window.reInitSelect2 = reInitSelect2;

function initSelect2(){
	if ($('.s2').length > 0) {
	    $('.s2').select2();
	 }
}

window.initSelect2 = initSelect2;

function hideTHeadFollowup(){
	$('tbody[id*=dataTableFollowupPoints_data] thead[id*=dataTableFollowupPoints]').hide();
}

window.hideTHeadFollowup = hideTHeadFollowup;

$(document).ready(function () {
	var wTableWithTopScroller = $('.linked-to-top-scroller table').width();
	$(".top-scroller .div-wrapper").css({width: wTableWithTopScroller});

	$(".top-scroller").scroll(function(){
        $(".linked-to-top-scroller .ui-datatable-tablewrapper")
            .scrollLeft($(".top-scroller").scrollLeft());
    });
    $(".linked-to-top-scroller .ui-datatable-tablewrapper").scroll(function(){
        $(".top-scroller")
            .scrollLeft($(".linked-to-top-scroller .ui-datatable-tablewrapper").scrollLeft());
    });
	
	$(document).on('collapsed.pushMenu', function () {
	    console.log('its collapsed');
	    //$('#logout-form-id\\:hideButton').click();
	    hideButton();
	});
	
	$(document).on('expanded.pushMenu', function () {
	    console.log('its expanded');
	    //$('#logout-form-id\\:showButton').click();
	    showButton();
	});
	
  //tl();//do translation
  /*$('.lang-options a').on('click', function (e) {
    e.preventDefault();
    var href = $(this).attr('href');
    activeLang = getHashFromUrl(href);
    localStorage.setItem('active-lang', activeLang);
    setActiveLang(activeLang);
    //console.log(localStorage.getItem('active-lang'));
    //do translation
  });*/

  $('#pendidikan-formal').cloneIt();
  $('#pendidikan-non-formal').cloneIt();
  $('#pengalaman-kerja').cloneIt();
  $('#dasar-ketentuan').cloneIt();
  $('#rmd-pic').cloneIt();
  $('#rmd-due-date').cloneIt();
  $('#k-pic-compliance').cloneIt();
  $('#k-supporting-unit').cloneIt();
  $('#mr-sla').cloneIt();
  $('#mp-rekam-jejak').cloneIt();
  $('#sp-judul-peraturan').cloneIt();
  $('.datepicker').datepicker();
  $('input').iCheck({
    checkboxClass: 'icheckbox_square-grey',
    radioClass: 'iradio_square-grey'
  });

  if ($('#list-assoc-perms').length > 0) {
    $('#list-assoc-perms').iCheckCheckAll();

    //sub
    var checkAll = $('#list-assoc-perms .check-all');
    var checkSingle = $('#list-assoc-perms .check-single');
    checkSingle.on('ifChanged', function (e) {
      var $el = $(e.target);
      var moduleName = $el.closest('.sub-check-single').attr('module');
      var subCheckAll = $('#list-assoc-perms .' + moduleName + '-check-all');
      var subCheckSingle = $('#list-assoc-perms .' + moduleName + '-check-single');
      // console.log(subCheckSingle.filter(':checked').length + ' == ' + subCheckSingle.length);
      // console.log('#list-assoc-perms .' + moduleName + '-check-all');
      //sub single
      if (subCheckSingle.filter(':checked').length == subCheckSingle.length) {
        subCheckAll.prop('checked', 'checked');
        if (checkSingle.filter(':checked').length == checkSingle.length) {
          checkAll.prop('checked', 'checked');
        }
      } else {
        subCheckAll.prop('checked', false);
        if (checkSingle.filter(':checked').length != checkSingle.length) {
          checkAll.prop('checked', false);
        }
      }

      subCheckAll.iCheck('update');
      checkAll.iCheck('update');
    });

    var subCheckAll = $('#list-assoc-perms .sub-check-all .check-single');
    subCheckAll.on('ifChecked', function (e) {
      var $el = $(e.target);
      var moduleName = $el.closest('.sub-check-all').attr('module');
      var subCheckSingle = $('#list-assoc-perms .' + moduleName + '-check-single');
      subCheckSingle.prop('checked', 'checked');
      subCheckSingle.iCheck('update');

      if (checkSingle.filter(':checked').length == checkSingle.length) {
        checkAll.prop('checked', 'checked');
        checkAll.iCheck('update');
      }
    }).on('ifUnchecked', function (e) {
      var $el = $(e.target);
      var moduleName = $el.closest('.sub-check-all').attr('module');
      var subCheckSingle = $('#list-assoc-perms .' + moduleName + '-check-single');
      subCheckSingle.prop('checked', false);
      subCheckSingle.iCheck('update');
    });
  }

  if ($('.date_picker').length > 0) {
    $('.date_picker').datetimepicker({
      format: "DD MMM YYYY",
      useCurrent: false
      //debug: true
    });
  }

  $.trumbowyg.svgPath = '/fonts/icons.svg';
  $('.rich-text').trumbowyg({
    btns: [['undo', 'redo'], // Only supported in Blink browsers
    ['strong', 'em', 'del'], ['superscript', 'subscript'], ['justifyLeft', 'justifyCenter', 'justifyRight', 'justifyFull'], ['unorderedList', 'orderedList'], ['horizontalRule'], ['removeformat']]
  });

  // -------------
  // - PIE CHART -
  // -------------
  // Get context with jQuery - using jQuery's .get() method.
  if ($('#pieChart').length > 0) {
    var pieChartCanvas = $('#pieChart').get(0).getContext('2d');
    var pieChart = new Chart(pieChartCanvas);
    var PieData = [{
      value: 700,
      color: '#f56954',
      highlight: '#f56954',
      label: 'Chrome'
    }, {
      value: 500,
      color: '#00a65a',
      highlight: '#00a65a',
      label: 'IE'
    }, {
      value: 400,
      color: '#f39c12',
      highlight: '#f39c12',
      label: 'FireFox'
    }, {
      value: 600,
      color: '#00c0ef',
      highlight: '#00c0ef',
      label: 'Safari'
    }, {
      value: 300,
      color: '#3c8dbc',
      highlight: '#3c8dbc',
      label: 'Opera'
    }, {
      value: 100,
      color: '#d2d6de',
      highlight: '#d2d6de',
      label: 'Navigator'
    }];
    var pieOptions = {
      // Boolean - Whether we should show a stroke on each segment
      segmentShowStroke: true,
      // String - The colour of each segment stroke
      segmentStrokeColor: '#fff',
      // Number - The width of each segment stroke
      segmentStrokeWidth: 1,
      // Number - The percentage of the chart that we cut out of the middle
      percentageInnerCutout: 50, // This is 0 for Pie charts
      // Number - Amount of animation steps
      animationSteps: 100,
      // String - Animation easing effect
      animationEasing: 'easeOutBounce',
      // Boolean - Whether we animate the rotation of the Doughnut
      animateRotate: true,
      // Boolean - Whether we animate scaling the Doughnut from the centre
      animateScale: false,
      // Boolean - whether to make the chart responsive to window resizing
      responsive: true,
      // Boolean - whether to maintain the starting aspect ratio or not when responsive, if set to false, will take up entire container
      maintainAspectRatio: false,
      // String - A legend template
      legendTemplate: '<ul class=\'<%=name.toLowerCase()%>-legend\'><% for (var i=0; i<segments.length; i++){%><li><span style=\'background-color:<%=segments[i].fillColor%>\'></span><%if(segments[i].label){%><%=segments[i].label%><%}%></li><%}%></ul>',
      // String - A tooltip template
      tooltipTemplate: '<%=value %> <%=label%> users'
    };
    // Create pie or douhnut chart
    // You can switch between pie and douhnut using the method below.
    pieChart.Doughnut(PieData, pieOptions);
    // -----------------
    // - END PIE CHART -
    // -----------------
  }

  if ($('.s2').length > 0) {
    $('.s2').select2();
  }

  $(document).on('shown.bs.modal', '#modalPilihPeraturan', function (event) {
    var $el = $(event.relatedTarget);
    var idx = $el.index('.pilih-peraturan');
    $('#modalPilihPeraturan').attr('idx-temp-no-peraturan', idx);
  });

  if ($('.pilih-peraturan').length > 0) {
    $('.pilih-peraturan').on('click', function (e) {
      e.preventDefault();
      var $el = $(this);
      //var idx = $(this).index('.pilih-peraturan');
      var idx = $('#modalPilihPeraturan').attr('idx-temp-no-peraturan');
      var $tr = $el.parents('tr');
      var noPeraturan = $tr.find('.no-peraturan').text();console.log(noPeraturan);
      var targetClass = $el.attr('target-class');
      //console.log('$(.'+targetClass+':eq(' + idx + ')');
      $('.' + targetClass + ':eq(' + idx + ')').val(noPeraturan);
      $('#modalPilihPeraturan').modal('hide');
    });
  }
  //language
  // console.log($('.lang-options a').length);
  // $('.lang-options a').on('click', function(e){
  //   e.preventDefault();
  //   var href = $(this).attr('href');
  //   var lang = getHashFromUrl(href);
  //   console.log(lang);
  // });
  //end language
});
/* WEBPACK VAR INJECTION */}.call(exports, __webpack_require__("./node_modules/jquery/dist/jquery.js")))

/***/ }),

/***/ 3:
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__("./resources/assets/js/front.js");


/***/ })

},[3]);