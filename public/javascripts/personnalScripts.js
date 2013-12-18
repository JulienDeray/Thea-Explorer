function openClose( id ) {
    var folderAtt = $('#' + id).attr('folder');
    if ( typeof folderAtt !== 'undefined' ) {
        if ( folderAtt === 'closed' )
            open( id );
        else
            close( id );
    }
}

function close( id ) {
    var elem = $('#' + id);
    elem.attr('folder', 'closed');

    elem.nextAll().each( function() {
        if ( $(this).attr('parent') === id ) { // if element is a child of $elem
            var folderAtt = $(this).attr('folder');

            if ( typeof folderAtt !== 'undefined' ) { // if it's a folder
                close( $(this).attr('id') );
        }

        $(this).hide();
        $(this).attr('hidden', true);
    }
});
}

function open( id ) {
    var elem = $('#' + id);
    elem.attr('folder', 'opened');

    elem.nextAll().each( function() {
        if ( $(this).attr('parent') === id ) { // if element is a child of $elem

            $(this).show();
            $(this).attr('hidden', false);
        }
    });
}
