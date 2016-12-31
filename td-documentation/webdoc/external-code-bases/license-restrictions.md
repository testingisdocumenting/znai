# Acceptable for Two Sigma

Before you bring external software into Two Sigma, you must verify is that the external software has a license that is acceptable to Two Sigma. The following licenses are known to be acceptable:

-    BSD
-    Apache
-    MIT
-    LGPL
-    Tcl/Tk
-    CDDL
-    EPL
-    Creative Commons

Do not bring in GPL licensed libraries

Libraries licensed using the GPL, which is distinct from LGPL, have been deemed unacceptable. 
If your external code uses a license that is not listed here, you should contact Christos and the Legal team.

GPL-licensed applications intended to be directly run from a shell 
(as opposed to libraries that will be invoked from our code) 
can be added as external codebases to our tree under ``ext_public_gpl2`` or ``ext_public_gpl3``, 
depending on the version of the license they are using. 
Guarding these applications (or the GPL-licensed files therein) from being referenced is recommended. 
It can be done by setting ``MODULE_FRIENDS`` to an empty string or ``MODULE_IS_TOP_LEVEL="yes"`` in ``software.mi``
 for the imported module. 
