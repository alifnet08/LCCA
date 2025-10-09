//global variables
var viewport = 650;
var proportionalCsH = 330;//propostional height of cs
var minHInnerNotif = 114;

function getPageID(){
	if($('.page-id').length > 0){
	    var classesPageID = $('.page-id').attr('class').split(/\s+/);
		for(i in classesPageID){
            var n = classesPageID[i];
		    if(n.indexOf('page--') !== -1){
		       return n.replace('page--', '');
		    }
		}
	}else{
		return 'not-set';
	}
};

function contentSizeSignIn() {
    var windowSize = $(window).outerHeight();
    var headSize = $('header').outerHeight();
    var footSize = $('footer').outerHeight();
    var contentLogin = $('main');
    contentLogin.height(windowSize - headSize - footSize);
}

function wSize() {
    var windowSize = $(window).outerHeight();
    if(windowSize < viewport) windowSize = viewport;
    var headSize = $('header').outerHeight();
    var footSize = $('footer').outerHeight();
    var leftSide = $('.left-sidebar');
    var contentSide = $('.content');
    var $cs = $('.cs', '.tpl-sidebar');
    var rightSide = $('.right-sidebar');
    var csPadding = 40;//top bottom @ 20px
    //var csHeight = (windowSize - ( headSize + footSize)) - csPadding; console.log(csHeight);
    //$cs.height(csHeight);
    //++$('.container-fluid .container-fluid').css('maxHeight', ((windowSize - ( headSize + footSize)) - 45));//39 breadcrumb + 6 whitespace
    var csConH = (($cs.width()) * 120)/100;//saat ini masih square set nya
	$cs.height(csConH);
	$('.content .cs .cs-illus .triangle').height((csConH*0.375));
    
    //cs mobile view
    if($('.content .cs').length > 0){
    	var csConH = (($('.content .cs').width()) * 110)/100;//saat ini masih square set nya
    	$('.content .cs').height(csConH);
    	$('.content .cs .cs-illus .triangle').height((csConH*0.375));
    }
    //console.log('windowSize', windowSize);
    //console.log('csHeight', csHeight);
    //console.log('headSize', headSize);
    //console.log('footSize', footSize);
    //console.log('calendarHeight', calendarHeight);
    //console.log('faqHeight', faqHeight);
    //console.log('('+windowSize+' - ( '+headSize+' + '+footSize+' + '+calendarHeight+' + '+faqHeight+')) - '+csPadding+')');
}

function wSizeV2() {
	var isMobile = $(window).width() < 992;
	var viewportMobile = $(window).height();
	
	var windowSize = $(window).outerHeight();
    var headH = $('header').outerHeight();
    var footH = $('footer').outerHeight();
    var contentH = isMobile ? viewportMobile : $('.tpl-content').outerHeight();
    
    var totalContentH = headH + contentH + footH;
    
    //sidebar
    //var notifH = $('.notif', '#tpl-sidebar').outerHeight();
    //var csH = $('.cs', '#tpl-sidebar').outerHeight();
    //var sidebarH = notifH + csH;
    
    //notif details
    var notifTitleH = $('.notif h5').outerHeight();
    var notifShortDescH = $('.notif h6').outerHeight();
    var notifSeeMoreH = $('.notif .notif-see').outerHeight();
    
    viewport = isMobile ? viewportMobile : totalContentH > viewport ? (totalContentH < windowSize ? windowSize : totalContentH) : viewport;//adjust viewport
    var contentOnlyH = isMobile ? viewport : viewport - (headH + footH);
   
    //calc notif
    var notifHFix = contentOnlyH - proportionalCsH;
    var notifInnerHFix = notifHFix - (notifTitleH + notifShortDescH + notifSeeMoreH + 40);//40 padd @ 20
    //console.log(notifInnerHFix);
    if(notifInnerHFix < minHInnerNotif){//doesnt reach min h
    	diffNotifOuterInner = minHInnerNotif - notifInnerHFix;
    	proportionalCsH = proportionalCsH - diffNotifOuterInner; //console.log(minHInnerNotif, notifInnerHFix, minHInnerNotif-notifInnerHFix);
    	notifInnerHFix = minHInnerNotif;
    	notifHFix += diffNotifOuterInner; 
    }
    
    $('.notif', '#tpl-sidebar').outerHeight(notifHFix);
    $('.notif .notif-item-wrapper').outerHeight(notifInnerHFix);//inner scroller
    $('.cs', '#tpl-sidebar').outerHeight(proportionalCsH);
}

function contentSize() { //console.log('first');
    var windowSize = $(window).outerHeight();
    if(windowSize < viewport) windowSize = viewport;
    var headSize = $('header').outerHeight();
    var footSize = $('footer').outerHeight();
    var content1Height = $('.content-1').outerHeight();
    var content3Height = $('#carouselMenu').outerHeight();
    var content2Side = $('.content-2');
    //var contentLogin = $('main');
    var contentHeightResult = (windowSize - headSize - footSize) - content1Height - content3Height;
    
    content2Side.height(contentHeightResult);
    
    
}

function doSizeAdjust(pageID){
	switch(pageID){
		default:
			wSizeV2();
			contentSize();//mobile
		break;
	}
}

function onCompleteWSizeV2(){
	$('.faq-content').ready(function(){
		wSizeV2();
		$('.nav-link').dropdown('toggle');//rebind nav toggler
	});
}

$(window).on('load', function() {
	wSizeV2();
	
	//open accordion first child
	$('#dataList > .card:eq(0) > .card-header > a').removeClass('collapsed');
	$('#dataList > .card:eq(0) > .collapse').addClass('show');
	$('#dataList > .card:eq(0) > .collapse > .card-body:eq(0) > .card-header:eq(0) > a').removeClass('collapsed');
	$('#dataList > .card:eq(0) > .collapse > .card-body:eq(0) > .collapse:eq(0)').addClass('show');
	$('#dataList > .card:eq(0) > .collapse > .card-body:eq(0) > .collapse:eq(0) > .card-body > .card:eq(0) > .card-header:eq(0) > .card-link').trigger('click');
});

$(document).ready(function () {
//    doSizeAdjust(getPageID());
    
    
    $("[data-trigger]").on("click", function(e){
        e.preventDefault();
        e.stopPropagation();
        var offcanvas_id =  $(this).attr('data-trigger');
        $(offcanvas_id).toggleClass("show");
        $('body').toggleClass("offcanvas-active");
        $(".screen-overlay").toggleClass("show");
    }); 

    $(".btn-close, .screen-overlay").click(function(e){
    	e.preventDefault();
        e.stopPropagation();
        $(".screen-overlay").removeClass("show");
        $(".mobile-offcanvas").removeClass("show");
        $(".tpl-sidebar").removeClass("show");
        $("body").removeClass("offcanvas-active");
    }); 
    
    //web portal show-webportal-lg
    $(".show-webportal-lg").on("click", function(e){
        e.preventDefault();
        e.stopPropagation();
        var offcanvas_id =  $(this).attr('data-trigger-lg');
        $(offcanvas_id).toggleClass("show");
        $('body').toggleClass("offcanvas-active");
        $(".screen-overlay").toggleClass("show");
    }); 

    $(".btn-close, .screen-overlay").click(function(e){
    	e.preventDefault();
        e.stopPropagation();
        $(".screen-overlay").removeClass("show");
        $(".desktop-offcanvas").removeClass("show");
        $("body").removeClass("offcanvas-active");
    }); 
    
    //search
    $('.site-search, .header-search-overlay').on('click', function(){
        if($('header').hasClass('search-mode')){
            $('header').removeClass('search-mode');
            $('.header-search-overlay').css('display', 'none');
        }else{
            $('header').addClass('search-mode');
            $('.header-search-overlay').css('display', 'block');
        }
    });
    
    //mobile look
    /*$('.navbar-mobile-look .site-search, .header-search-overlay.mobile-look').on('click', function(){
        if($('header').hasClass('search-mode')){
            $('header').removeClass('search-mode');
            $('.header-search-overlay.mobile-look').css('display', 'none');
        }else{
            $('header').addClass('search-mode');
            $('.header-search-overlay.mobile-look').css('display', 'block');
        }
    });*/
    
    $('#inputSearch').bind("onEnter",function(e){
    	   $(this).parents('.classform').find('.search-icon input').trigger('click');
	});
    $('#inputSearch').keyup(function(e){
	    if(e.keyCode == 13)
	    {
	        $(this).trigger("onEnter");
	    }
	});
});
$(window).resize(function () {
	doSizeAdjust(getPageID());
});

function slideCarousel() {
    $('.carousel').on('slide.bs.carousel', function (e) {
        var $e = $(e.relatedTarget);
        var $t = $(this);
        var $inner = $t.find('.carousel-inner');
        var idx = $e.index();
        var itemsPerSlide = 4;
        var totalItems = $t.find('.carousel-item').length;

        if (idx >= totalItems - (itemsPerSlide - 1)) {
            var it = itemsPerSlide - (totalItems - idx);
            for (var i = 0; i < it; i++) {
                // append slides to end
                if (e.direction == "left") {
                    $t.find('.carousel-item').eq(i).appendTo($inner);
                }
                else {
                    $t.find('.carousel-item').eq(0).appendTo($inner);
                }
            }
        }
    });
}

function userClick() {
    $('.navbar-toggler').click(function () {
        var user = $('.user');
        var navBar = $('#collapsibleNavbar').hasClass('show');
        if (navBar == false) {
            user.addClass('btn-clicked');
        } else {
            user.removeClass('btn-clicked');
        }
    });
}
var callAgain = false;


function calendarNew(events) {
    
    $('#calendar-agenda').datepicker({

        forceParse: false,
        todayHighlight: true,
        language: 'id',
        templates: {
			leftArrow: '',
			rightArrow: ''
		},
        beforeShowDay: function (date) {
            var result = {};
            var dateFormated = date.toLocaleDateString("en-US");

            $.each(events, function (eventIdx, eventDetail) {
                var dateEventFormated = eventDetail.Date.toLocaleDateString("en-US");
                if (dateFormated == dateEventFormated) {
                    result.classes = 'highlight';
                    result.tooltip = eventDetail.Title;
                    if (typeof eventDetail.Staticclass !== 'undefined' && eventDetail.Staticclass !== '')
                        result.classes += ' ' + eventDetail.Staticclass;
                }
                if (typeof eventDetail.Title !== 'undefined' && eventDetail.Title !== '') { }
                // result.tooltip = eventDetail.Title;
                result.content = '<span class="date-lbl">' + date.getDate() + '</span>';
            });

            return result;
        }
    }).on('changeDate', function (date) {
        var xDate = new Date(date.date);
        // console.log(xDate.toLocaleDateString("en-US"));
        var dateFormated = xDate.toLocaleDateString("en-US");
        var result = {};
        var top = $('.table-condensed').find('active').position();
        var right = $('.table-condensed').find('active').position();

        selectedData = null;
        $.each(events, function (eventIdx, eventDetail) {
            var dateFormatedEvent = eventDetail.Date.toLocaleDateString("en-US");
            if (dateFormatedEvent == dateFormated) {
                result.value = eventDetail.Title;
            }
        });
        if (result.value) {
            var talk = $('.table-condensed').find('.active');
            if (callAgain == false) {
                talk.append('<div class="talk you"><p>' + result.value + '</p></div>');
                callAgain = true;
                setTimeout(function test() {
                    callAgain = false;
                    $('.talk').remove();
                }, 5000);
            } else {
                callAgain = false;
                $('.talk').remove();
            }
        }
        
        
        	$('#calendar-agenda .datepicker-days .day.highlight').on('mouseenter', function(){
                var $el = $(this); 
                $('.talk').remove();
                $el.append('<div class="talk you"><p>' + $el.attr('title') + '</p></div>');
            }).on('mouseleave', function(){
            	setTimeout(function() {
                    $('.talk').remove();
                }, 5000);
            });
        
    });
    
    $('#calendar-agenda .datepicker-days .day.highlight').on('mouseenter', function(){
        var $el = $(this); 
        $('.talk').remove();
        $el.append('<div class="talk you"><p>' + $el.attr('title') + '</p></div>');
    }).on('mouseleave', function(){
    	setTimeout(function() {
            $('.talk').remove();
        }, 5000);
    });
    
    
    
    
}

function calendar() {
	var events = [
	   /* { Title: "Tahun Baru 2020", Date: new Date("01/01/2020") },
	    { Title: "Tahun Baru Imlek", Date: new Date("01/25/2020") },
	    { Title: "Meeting Maybank", Date: new Date("02/01/2020"), Staticclass: 'head' },
	    { Title: "Meeting Maybank", Date: new Date("02/02/2020"), Staticclass: 'mid' },
	    { Title: "Meeting Maybank", Date: new Date("02/03/2020"), Staticclass: 'tail' },
	    { Title: "Meeting Maybank", Date: new Date("02/26/2020"), Staticclass: 'head' },
	    { Title: "Meeting Maybank", Date: new Date("02/27/2020"), Staticclass: 'mid' },
	    { Title: "Meeting Maybank", Date: new Date("02/28/2020"), Staticclass: 'tail' },
	    { Title: "Meeting Maybank x", Date: new Date("01/06/2020"), Staticclass: 'head' },
	    { Title: "Meeting Maybank x", Date: new Date("01/07/2020"), Staticclass: 'mid' },
	    { Title: "Meeting Maybank x", Date: new Date("01/08/2020"), Staticclass: 'tail' }*/
	];
    $('#calendar-agenda').datepicker({

        forceParse: false,
        todayHighlight: true,
        language: 'id',
        templates: {
			leftArrow: '',
			rightArrow: ''
		},
        beforeShowDay: function (date) {
            var result = {};
            var dateFormated = date.toLocaleDateString("en-US");

            $.each(events, function (eventIdx, eventDetail) {
                var dateEventFormated = eventDetail.Date.toLocaleDateString("en-US");
                if (dateFormated == dateEventFormated) {
                    result.classes = 'highlight';
                    result.tooltip = eventDetail.Title;
                    if (typeof eventDetail.Staticclass !== 'undefined' && eventDetail.Staticclass !== '')
                        result.classes += ' ' + eventDetail.Staticclass;
                }
                if (typeof eventDetail.Title !== 'undefined' && eventDetail.Title !== '') { }
                // result.tooltip = eventDetail.Title;
                result.content = '<span class="date-lbl">' + date.getDate() + '</span>';
            });

            return result;
        }
    }).on('changeDate', function (date) {
        var xDate = new Date(date.date);
        // console.log(xDate.toLocaleDateString("en-US"));
        var dateFormated = xDate.toLocaleDateString("en-US");
        var result = {};
        var top = $('.table-condensed').find('active').position();
        var right = $('.table-condensed').find('active').position();

        selectedData = null;
        $.each(events, function (eventIdx, eventDetail) {
            var dateFormatedEvent = eventDetail.Date.toLocaleDateString("en-US");
            if (dateFormatedEvent == dateFormated) {
                result.value = eventDetail.Title;
            }
        });
        if (result.value) {
            var talk = $('.table-condensed').find('.active');
            if (callAgain == false) {
                talk.append('<div class="talk you"><p>' + result.value + '</p></div>');
                callAgain = true;
                setTimeout(function test() {
                    callAgain = false;
                    $('.talk').remove();
                }, 5000);
            } else {
                callAgain = false;
                $('.talk').remove();
            }
        }
    });
}

function menu(id) {
    var mainMenu = $("#" + id);
    var showMenu = $('#show-' + id);
    btnCloseMainMenu();
    // if (callAgain) {
    //     btnClose();
    //     callAgain = false;
    // } else {
    mainMenu.removeClass("collapsed");
    showMenu.addClass("show");
    // callAgain = true;
    // }

    if (showMenu.hasClass("show")) {
        $('#btn-close').addClass('show');
    }
}

function btnCloseMainMenu() {
    $('#btn-close').removeClass('show');
    $('div[id^="show-"]').removeClass('show');
    $('div[id^="menu"]').addClass("collapsed");
}

function tabs(id) {
    var tab = $("#" + id);
    var tabPage = $('#tabs-' + id);
    var tabItem = $('.tabs-item');
    var tabContent = $('.tabs-content');

    tabItem.removeClass("active");
    tabContent.removeClass("active");

    tab.addClass("active");
    if (tab.hasClass("active")) {
        tabPage.addClass('active');
    }
}