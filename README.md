# Intellij Ngx-Translate Support

Automatically create references for ngx-translate strings into i18n translation
files. This plugin enables jump-to-definition, adds minor syntax highlighting
for the translate tokens, and code completion.

Accessors like `TEST-STRING.SUBOBJECT.TEST123_TEST` are currently auto-detected
in Javascript files (including Typescript and Angular templates).

The plugin considers `.json`s that have `i18n` in their path to be translation
files.
