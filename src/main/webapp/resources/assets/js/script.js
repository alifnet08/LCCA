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
    if(windowSize < 768) windowSize = 768;
    var headSize = $('header').outerHeight();
    var footSize = $('footer').outerHeight();
    var calendarHeight = $('.calendar').outerHeight();
    var faqHeight = $('.faq-bg').outerHeight();
    var leftSide = $('.left-sidebar');
    var contentSide = $('.content');
    var $cs = $('.cs', '.left-sidebar');
    var rightSide = $('.right-sidebar');
    var csPadding = 40;//top bottom @ 20px
    var csHeight = (windowSize - ( headSize + footSize + calendarHeight + faqHeight)) - csPadding;
    $cs.height(csHeight);
    
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

function contentSize() {
    var windowSize = $(window).outerHeight();
    if(windowSize < 768) windowSize = 768;
    var headSize = $('header').outerHeight();
    var footSize = $('footer').outerHeight();
    var content1Height = $('.content-1').outerHeight();
    var content3Height = $('#carouselMenu').outerHeight();
    var content2Side = $('.content-2');
    //var contentLogin = $('main');
    var contentHeightResult = (windowSize - headSize - footSize) - content1Height - content3Height;
    
    content2Side.height(contentHeightResult);
    //contentLogin.height(windowSize - headSize - footSize);
    $('.map-full').css('top', -
    		(0.5 * $('.map-full').height()) + 
    		(0.5 * $('.content-2').height())
        );
    $('.map-full').on('load', function(){
    	//map window mask
        $('.map-full').css('top', -
    		(0.5 * $('.map-full').height()) + 
    		(0.5 * $('.content-2').height())
        );
        //end map window mask
    });
    
    
}

function adjustMapMaskArea(){
	//$('.map-full').on('load', function(){
		var mapW = $('.map-full').width();
		var mapH = $('.map-full').height();
		var mapPos = $('.map-full').position();
		var $pinCon = $('.world-map .pin');
		var pinMarginLeft = $('.map-full').css('margin-left');
		$pinCon.css({
		    left: mapPos.left,
		    top: mapPos.top, 
		    width: mapW,
		    height: mapH,
		    marginLeft: pinMarginLeft
		});
	//});
}

function adjustViewPortNotiAnnoun(){
	var windowH = $(window).outerHeight();
    if(windowH < 768) windowH = 768;
    
    var headH = $('header').outerHeight();
    var footH = $('footer').outerHeight();
    
    var safeWindowH = windowH - headH - footH;
	//notif
    var notifWrapperH = safeWindowH/2;//$('.notif').outerHeight();
    $('.notif', '.right-sidebar').height(notifWrapperH-40);
	
	var notifTitle = $('.notif h5', '.right-sidebar').outerHeight();
	var notifShortDesc = $('.notif h6', '.right-sidebar').outerHeight();
	var notifSee = $('.notif .notif-see', '.right-sidebar').outerHeight();
	$('.notif-item-wrapper', '.right-sidebar').height(notifWrapperH - notifTitle - notifShortDesc - notifSee - 40);//40: top bottom @ 20px padd

	//pengumuman
	var announWrapperH = safeWindowH/2;//$('.pengumuman').outerHeight();
	$('.pengumuman', '.right-sidebar').height(announWrapperH);
	
	var announTabHead = $('.pengumuman .tabs', '.right-sidebar').outerHeight();
	var announTitle = $('.pengumuman h6', '.right-sidebar').outerHeight();
	var announSee = $('.notif .pengumuman-see', '.right-sidebar').outerHeight();
	$('.pengumuman-item-wrapper', '.right-sidebar').height(announWrapperH - announTabHead - announTitle -announSee - 40);//40: top bottom @ 20px padd
}

function doSizeAdjust(pageID){
	switch(pageID){
		case 'sign-in':
			contentSizeSignIn();
		break;
		default:
			wSize();
			contentSize();
			adjustMapMaskArea();
			$('.map-full').on('load', function(){
				adjustMapMaskArea();
			});
			
			adjustViewPortNotiAnnoun();
		break;
	}
}

$(document).ready(function () {
    moment.locale('en-US', {
        week: { dow: 1 } // Monday is the first day of the week
    });
    //calendar();
    slideCarousel();
    // userClick();
    
    //var pageID = getPageID();
    doSizeAdjust(getPageID());
    /*switch(pageID){
    	case 'sign-in':
    		contentSizeSignIn();
    	break;
    	default:
    		wSize();
    		contentSize();
    		adjustMapMaskArea();
    	break;
    }*/
    
    //$('.notif-wrapper').tinyscrollbar();
    
    
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
        $("body").removeClass("offcanvas-active");
    }); 
    
    //$talk.append('<div class="talk you"><p>' + result.value + '</p></div>');
    //callAgain = true;
    //setTimeout(function test() {
    //    callAgain = false;
    //    $('.talk').remove();
    //}, 5000);
    
    
    
});
$(window).resize(function () {
	doSizeAdjust(getPageID());
	/*var pageID = getPageID();
	console.log(pageID);
    switch(pageID){
    	case 'sign-in':
    		contentSizeSignIn();
    	break;
    	default:
    		wSize();
    		contentSize();
    		adjustMapMaskArea();
    	break;
    }*/
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