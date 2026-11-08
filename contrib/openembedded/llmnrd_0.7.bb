SUMMARY = "Link-Local Multicast Name Resolution (LLMNR) daemon"
DESCRIPTION = "Answers LLMNR (RFC 4795) queries, so Windows clients can \
resolve the receiver's host name without DNS. IPv4 and IPv6."
HOMEPAGE = "https://github.com/Schimmelreiter/llmnrd"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=05a4c5604b9126dcb84dcc6f1a84a7bb"

SRC_URI = "git://github.com/Schimmelreiter/llmnrd.git;protocol=https;branch=master"
SRCREV = "f662f9d7eafb5a2d4a6bc3a212703c56c8e054cb"
PV = "0.7+git"

# OE 5.1 and newer (UNPACKDIR, like oe-alliance 6.0); older releases: ${WORKDIR}/git
S = "${UNPACKDIR}/git"

inherit update-rc.d

# Note: wsdd2 (part of many images) also answers LLMNR on UDP port 5355.
# With wsdd2 running, llmnrd is not needed (or start wsdd2 with -w).

EXTRA_OEMAKE = "CC='${CC}' CFLAGS='${CFLAGS}' LDFLAGS='${LDFLAGS}' GIT_VERSION= PIDFILE=/var/run/llmnrd.pid"

do_compile() {
    oe_runmake llmnrd llmnr-query
}

do_install() {
    install -D -m 0755 ${S}/llmnrd ${D}${sbindir}/llmnrd
    install -D -m 0755 ${S}/llmnr-query ${D}${bindir}/llmnr-query
    install -D -m 0755 ${S}/packaging/sysv/llmnrd.init ${D}${sysconfdir}/init.d/llmnrd
    install -D -m 0644 ${S}/packaging/sysv/llmnrd.default ${D}${sysconfdir}/default/llmnrd
}

CONFFILES:${PN} = "${sysconfdir}/default/llmnrd"

INITSCRIPT_NAME = "llmnrd"
INITSCRIPT_PARAMS = "defaults 60"
