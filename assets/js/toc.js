// wrap markdown-toc in navbar div
$('#markdown-toc').wrapAll('<nav id="post-nav" class="navbar post-nav">');

// add scrollspy to post navigation
$('body').scrollspy({target: "#post-nav", offset: 50});

// format toc objects with classes for custom scrollspy
$('#markdown-toc').addClass('nav justify-content-center');
$('#markdown-toc li').addClass('nav-item');
$('#markdown-toc a').addClass('nav-link');

// capitalize headings - because I do not in markdown
$('#markdown-toc a').css({"text-transform": "capitalize"});

// loop thru toc list, get headings
// pad each heading to clear post-nav when linked
$('#markdown-toc li a').each(function(index) {
  heading = $(this).attr('id');
  // remove auto pre text from toc id
  heading = heading.replace("markdown-toc-", "")
  $("#"+heading).css({"padding-top": "50px"});
});

// fix post navbar on scroll past
var distanceFromTop = $('#post-nav').offset().top;
$(window).scroll(function() {
  var currentScroll = $(window).scrollTop();
  if (currentScroll >= distanceFromTop) {
    $('#post-nav').addClass('fixed-top');
  } else {
    $('#post-nav').removeClass('fixed-top');
  }
});