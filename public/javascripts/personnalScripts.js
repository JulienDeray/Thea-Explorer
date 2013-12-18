function showHide( id ) {
    var elem = $('#tr-' + id);
    var depth = elem.children().eq(0).children().eq(0).attr('style').substring(13, 14);

    if (depth > 0)
        depth = elem.children().eq(0).children().eq(0).attr('style').substring(13, 15);

    depth = parseInt( depth );

    elem.nextAll().each( function() {
        var researchedDepth = depth + 17;

        if ( $(this).children().eq(0).children().eq(0).attr('style').substring(13, 15) == researchedDepth ) {
            var hidden = $(this).attr('hidden');

            if (typeof hidden === 'undefined') {
                $(this).hide();
                $(this).attr('hidden', true);
            }
            else {
                $(this).show();
                $(this).attr('hidden', false);
            }
        }
    });

}
