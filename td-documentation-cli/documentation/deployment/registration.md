# Register Documentation in CMDB

Warning: Documentation must be registered in CMDB or it will be deleted from the database after a week. Only documentation meant to be ephemeral (e.g., for purposes of a code review) should go unregistered.

To register, create a new [ts_documentation entity in CMDB](https://cmdb.twosigma.com/entity_types/ts_documentation)
1.  Provide your unique doc-id in the first field, "Ts documentation."
2.  **Optional**: If you are deploying from VATS, add your codebase name in the optional field "Ts documentation vats codebase name." Otherwise, skip this step.
3.  Choose a documentation type (*mdoc* or *sphinx*) for "Ts documentation type."
4.  Provide easy-to-read display title for "Ts documentation title."
5.  Provide content category for "Ts documentation category" (see [TS Guide landing page](https://tsguides.app.twosigma.com) for existing categories).
6. Provide a short description of your documentation for "Ts documentation description."
7. Check the box for "Ts documentation display on landing" if you want your entry to appear on the [TS Guide landing page](https://tsguides.app.twosigma.com).

Note: In addition to ensuring that your documentation persists, registering in CMDB is necessary for content indexing by federated search.