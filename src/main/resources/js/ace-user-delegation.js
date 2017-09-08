function openUserPickerWindow(element) {
    var vWinUsers = window.open('$!requestContext.canonicalBaseUrl/secure/popups/UserPickerBrowser.jspa?formName=jiraform&multiSelect=false&element=' + element, 'UserPicker', 'status=yes,resizable=yes,top=100,left=200,width=580,height=750,scrollbars=yes');
    vWinUsers.opener = self;
    vWinUsers.focus();
}
