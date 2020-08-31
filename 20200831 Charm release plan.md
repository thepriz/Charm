# Charm 2.0 release plan

## Forge
`2.0` is currently building against MC 16.2. There is no Quark version available for this MC version.

### Charm

For 16.1:

* Branch `2.0-forge` to `2.0-forge-16.1`:
	* `build.gradle` target MC 16.1
	* Diff 16.2 and save patch 
	* Backport code fixes from fabric to forge
	* Restore Quark compat class.
* Archive all `14.x` and `15.x` branches
* Build beta and test in a MultiMC instance alongside Quark

For 16.2:

* Branch `2.0-forge-16.1` to `2.0-forge-16.2`
	* Restore 16.2 diff patch and fix conflicts
	* Disable Quark compat class (comment-out)
* **No build until Forge 16.2 is stable**

For 12.2:

* Rename `mc1.12` to `12.2`
* Build master and test in a MultiMC instance alongside FutureMC

### Covalent and Charmonium
* Branch `2.0` to `2.0-forge` and delete `2.0`
* Archive all `14.x` and `15.x` branches
* Build beta (16.1) and test in MultiMC instance alongside Quark and Abnormals

### Charm-assets
* Create `svenhjol/Charm-assets` repo
	* Copy all assets from fabric to this repo
	* Recipes differ: keep track of this

## Fabric
`2.0` is currently building against MC 16.2. No earlier versions will be supported.

### Charm
* Build beta, get testing/breaking

### Covalent and Charmonium
* Create new branch `2.0-fabric` and rebuild the mods.

## Website
Charm website [https://github.io/svenhjol/Charm/]() needs reworking.

* No longer section-based
* Deprecate old 14.x/15.x features into their own page
* Add any new features plus screenshots