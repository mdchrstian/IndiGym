# Membuat file .drawio (hitam putih) untuk laporan: class diagram, use case, dan user flow per layar.
# Jalankan: python3 docs/diagram/buat_diagram.py  -> menulis *.drawio di folder ini.
from pathlib import Path
from xml.sax.saxutils import quoteattr

OUT = Path(__file__).parent
BW = "fillColor=#FFFFFF;strokeColor=#000000;fontColor=#000000;"
S_TERM = "rounded=1;arcSize=50;whiteSpace=wrap;html=1;fillColor=#000000;strokeColor=#000000;fontColor=#FFFFFF;fontSize=14;fontStyle=1;"
S_PROC = "rounded=0;whiteSpace=wrap;html=1;" + BW + "fontSize=14;"
S_IO = "shape=parallelogram;perimeter=parallelogramPerimeter;fixedSize=1;size=16;spacingLeft=20;spacingRight=20;whiteSpace=wrap;html=1;" + BW + "fontSize=14;"
S_DEC = "rhombus;spacingLeft=14;spacingRight=14;whiteSpace=wrap;html=1;" + BW + "fontSize=13;"
S_EDGE = "edgeStyle=orthogonalEdgeStyle;rounded=1;html=1;strokeColor=#000000;fontColor=#000000;fontSize=12;labelBackgroundColor=#FFFFFF;"
SIDE = {"r": (1, 0.5), "l": (0, 0.5), "t": (0.5, 0), "b": (0.5, 1)}


class Diagram:
    def __init__(self):
        self.cells = []

    def v(self, id, label, style, x, y, w, h, parent="1"):
        self.cells.append(f'<mxCell id="{id}" value={quoteattr(label)} style="{style}" vertex="1" parent="{parent}">'
                          f'<mxGeometry x="{x}" y="{y}" width="{w}" height="{h}" as="geometry"/></mxCell>')

    def e(self, src, tgt, label="", style=S_EDGE, out=None, inn=None, pts=(), dashed=False, parent="1"):
        st = style + ("dashed=1;" if dashed else "")
        if out:
            st += "exitX=%s;exitY=%s;" % (SIDE.get(out) or out)
        if inn:
            st += "entryX=%s;entryY=%s;" % (SIDE.get(inn) or inn)
        p = "".join(f'<mxPoint x="{x}" y="{y}"/>' for x, y in pts)
        arr = f'<Array as="points">{p}</Array>' if pts else ""
        self.cells.append(f'<mxCell id="e{len(self.cells)}" value={quoteattr(label)} style="{st}" edge="1" parent="{parent}" source="{src}" target="{tgt}">'
                          f'<mxGeometry relative="1" as="geometry">{arr}</mxGeometry></mxCell>')

    def save(self, name):
        body = "".join(self.cells)
        xml = (f'<mxfile><diagram name="{name}"><mxGraphModel adaptiveColors="none" background="#FFFFFF"><root>'
               f'<mxCell id="0"/><mxCell id="1" parent="0"/>{body}</root></mxGraphModel></diagram></mxfile>')
        (OUT / f"{name}.drawio").write_text(xml, encoding="utf-8")


# ---------- user flow: node berpusat di kolom ----------
# proc = proses (kotak siku), io = input/output (jajar genjang), dec = keputusan Ya/Tidak, term = mulai/selesai
COL = {"A": 160, "B": 430, "C": 700, "D": 970, "E": 1240}
SIZE = {"term": (210, 50), "proc": (224, 56), "io": (256, 56), "dec": (220, 96)}
STYLE = {"term": S_TERM, "proc": S_PROC, "io": S_IO, "dec": S_DEC}


def flow(name, nodes, edges):
    d = Diagram()
    for id, kind, label, col, y in nodes:
        w, h = SIZE[kind]
        d.v(id, label, STYLE[kind], COL[col] - w // 2, y, w, h)
    for ed in edges:
        src, tgt, *rest = ed
        kw = rest[0] if rest else {}
        d.e(src, tgt, **kw)
    d.save(name)


def small(t):
    return f"<br><font style='font-size:11px'>{t}</font>"


def cabang(src, y, targets):
    # garis dari node sumber turun ke y, lalu bercabang ke kotak aksi di tiap kolom
    return [(src, t, {"out": "b", "inn": "t", "pts": [(COL[c0], y), (COL[c], y)]}) for t, c, c0 in targets]


flow("userflow_1_login", [
    ("s", "term", "User Flow: Layar Login", "A", 0),
    ("n1", "io", "Isi username &amp; password, klik Masuk", "A", 100),
    ("d1", "dec", "Data benar &amp; akun aktif?", "A", 200),
    ("e1", "io", "Tampil pesan error", "B", 220),
    ("t", "term", "Masuk ke Dashboard", "A", 360),
], [
    ("s", "n1"), ("n1", "d1"),
    ("d1", "e1", {"label": "Tidak", "out": "r", "inn": "l"}),
    ("e1", "n1", {"out": "t", "inn": "r", "dashed": True}),
    ("d1", "t", {"label": "Ya", "out": "b", "inn": "t"}),
])

flow("userflow_2_dashboard", [
    ("s", "term", "User Flow: Layar Dashboard", "A", 0),
    ("n1", "io", "Tampil ringkasan hari ini" + small("check-in, pelanggan berakses, penjualan"), "A", 100),
    ("n2", "io", "User memilih menu", "A", 200),
    ("d1", "dec", "Pilih Keluar?", "A", 300),
    ("t1", "term", "Buka layar yang dipilih" + small("Pelanggan, Membership, Kasir, Laporan, Akun"), "A", 460),
    ("t2", "term", "Logout, kembali ke Login", "B", 323),
], [
    ("s", "n1"), ("n1", "n2"), ("n2", "d1"),
    ("d1", "t1", {"label": "Tidak", "out": "b", "inn": "t"}),
    ("d1", "t2", {"label": "Ya", "out": "r", "inn": "l"}),
])

flow("userflow_3_pelanggan", [
    ("s", "term", "User Flow: Layar Pelanggan", "B", 0),
    ("n1", "io", "Cari pelanggan atau daftarkan pelanggan baru", "B", 100),
    ("n2", "io", "Pilih pelanggan, tampil status akses hari ini", "B", 200),
    ("n3", "io", "User memilih aksi", "B", 300),
    ("a1", "proc", "Check-in", "A", 420),
    ("a2", "proc", "Jual visit harian", "C", 420),
    ("a3", "proc", "Ubah / arsip profil", "D", 420),
    ("a4", "proc", "Jual membership", "E", 420),
    ("c1", "dec", "Profil aktif &amp; punya akses hari ini?", "A", 530),
    ("c2", "proc", "Check-in ditolak &amp; dicatat", "A", 690),
    ("c3", "proc", "Check-in berhasil &amp; dicatat", "B", 690),
    ("v1", "dec", "Konfirmasi bayar visit harian?", "C", 530),
    ("v2", "proc", "Simpan visit + check-in otomatis", "C", 690),
    ("v3", "io", "Tampil bukti pembayaran", "C", 790),
    ("u1", "io", "Isi data baru atau konfirmasi arsip", "D", 550),
    ("u2", "proc", "Simpan perubahan profil", "D", 690),
    ("tm", "term", "Pindah ke Layar Membership", "E", 553),
    ("t", "term", "Selesai", "B", 900),
], [
    ("s", "n1"), ("n1", "n2"), ("n2", "n3"),
    *cabang("n3", 390, [("a1", "A", "B"), ("a2", "C", "B"), ("a3", "D", "B"), ("a4", "E", "B")]),
    ("a1", "c1"), ("a2", "v1"), ("a3", "u1"), ("a4", "tm"),
    ("c1", "c2", {"label": "Tidak", "out": "b", "inn": "t"}),
    ("c1", "c3", {"label": "Ya", "out": "r", "inn": "t", "pts": [(430, 578)]}),
    ("v1", "v2", {"label": "Ya", "out": "b", "inn": "t"}),
    ("v1", "t", {"label": "Tidak", "out": "r", "inn": "r", "pts": [(830, 578), (830, 925)]}),
    ("v2", "v3"), ("u1", "u2"),
    ("c2", "t", {"out": "b", "inn": "l", "pts": [(160, 925)]}),
    ("c3", "t", {"out": "b", "inn": "t"}),
    ("v3", "t", {"out": "b", "inn": "r", "pts": [(700, 925)]}),
    ("u2", "t", {"out": "b", "inn": "r", "pts": [(970, 925)]}),
])

flow("userflow_4_membership", [
    ("s", "term", "User Flow: Layar Membership", "B", 0),
    ("n1", "io", "User memilih tab", "B", 100),
    ("a1", "proc", "Jual membership", "A", 220),
    ("a2", "proc", "Membership aktif", "C", 220),
    ("a3", "proc", "Kelola paket", "D", 220),
    ("j1", "io", "Pilih pelanggan &amp; paket, tampil periode berlaku", "A", 320),
    ("jd", "dec", "Membership lama masih berjalan?", "A", 420),
    ("je", "io", "Tombol Bayar tidak aktif", "B", 440),
    ("jk", "dec", "Konfirmasi pembayaran?", "A", 570),
    ("j2", "proc", "Simpan pembayaran", "A", 720),
    ("j3", "io", "Tampil bukti pembayaran", "A", 820),
    ("m1", "io", "Tampil daftar membership yang masih aktif", "C", 320),
    ("k1", "io", "Isi paket baru atau pilih paket yang diarsipkan", "D", 320),
    ("k2", "proc", "Simpan perubahan paket", "D", 420),
    ("t", "term", "Selesai", "B", 930),
], [
    ("s", "n1"),
    *cabang("n1", 190, [("a1", "A", "B"), ("a2", "C", "B"), ("a3", "D", "B")]),
    ("a1", "j1"), ("a2", "m1"), ("a3", "k1"),
    ("j1", "jd"),
    ("jd", "je", {"label": "Ya", "out": "r", "inn": "l"}),
    ("jd", "jk", {"label": "Tidak", "out": "b", "inn": "t"}),
    ("jk", "j2", {"label": "Ya", "out": "b", "inn": "t"}),
    ("jk", "t", {"label": "Tidak", "out": "r", "inn": "t", "pts": [(430, 618)]}),
    ("je", "t", {"out": "b", "inn": "t"}),
    ("j2", "j3"), ("k1", "k2"),
    ("j3", "t", {"out": "b", "inn": "l", "pts": [(160, 955)]}),
    ("m1", "t", {"out": "b", "inn": "r", "pts": [(700, 955)]}),
    ("k2", "t", {"out": "b", "inn": "r", "pts": [(970, 955)]}),
])

flow("userflow_5_kasir", [
    ("s", "term", "User Flow: Layar Kasir &amp; Produk", "B", 0),
    ("n1", "io", "User memilih tab", "B", 100),
    ("a1", "proc", "Kasir", "A", 220),
    ("a2", "proc", "Produk dan stok", "C", 220),
    ("a3", "proc", "Riwayat", "D", 220),
    ("k1", "io", "Pilih produk &amp; jumlah", "A", 320),
    ("kd", "dec", "Stok cukup?", "A", 420),
    ("ke", "io", "Tampil pesan stok tidak cukup", "B", 440),
    ("k2", "proc", "Masuk keranjang", "A", 570),
    ("kl", "dec", "Tambah produk lagi?", "A", 670),
    ("kk", "dec", "Checkout, konfirmasi pembayaran?", "A", 820),
    ("k3", "proc", "Simpan transaksi, stok berkurang", "A", 970),
    ("k4", "io", "Tampil struk", "A", 1070),
    ("p1", "io", "Isi data produk atau jumlah stok", "C", 320),
    ("p2", "proc", "Simpan, ubah, tambah stok, atau arsipkan produk", "C", 420),
    ("r1", "io", "Pilih transaksi di riwayat", "D", 320),
    ("rd", "dec", "Batalkan transaksi?", "D", 420),
    ("r2", "io", "Isi alasan pembatalan", "D", 570),
    ("r3", "proc", "Status BATAL, stok dikembalikan", "D", 670),
    ("r4", "io", "Tampil struk transaksi", "E", 440),
    ("t", "term", "Selesai", "C", 1180),
], [
    ("s", "n1"),
    *cabang("n1", 190, [("a1", "A", "B"), ("a2", "C", "B"), ("a3", "D", "B")]),
    ("a1", "k1"), ("a2", "p1"), ("a3", "r1"),
    ("k1", "kd"),
    ("kd", "ke", {"label": "Tidak", "out": "r", "inn": "l"}),
    ("ke", "k1", {"out": "t", "inn": "r", "dashed": True}),
    ("kd", "k2", {"label": "Ya", "out": "b", "inn": "t"}),
    ("k2", "kl"),
    ("kl", "k1", {"label": "Ya", "out": "l", "inn": "l", "dashed": True, "pts": [(24, 718), (24, 348)]}),
    ("kl", "kk", {"label": "Tidak", "out": "b", "inn": "t"}),
    ("kk", "k3", {"label": "Ya", "out": "b", "inn": "t"}),
    ("kk", "t", {"label": "Tidak", "out": "r", "inn": "t", "pts": [(700, 868)]}),
    ("k3", "k4"), ("p1", "p2"),
    ("k4", "t", {"out": "b", "inn": "l", "pts": [(160, 1205)]}),
    ("p2", "t", {"out": "b", "inn": "t"}),
    ("r1", "rd"),
    ("rd", "r2", {"label": "Ya", "out": "b", "inn": "t"}),
    ("rd", "r4", {"label": "Tidak", "out": "r", "inn": "l"}),
    ("r2", "r3"),
    ("r3", "t", {"out": "b", "inn": "r", "pts": [(970, 1205)]}),
    ("r4", "t", {"out": "b", "inn": "r", "pts": [(1240, 1205)]}),
])

flow("userflow_6_laporan", [
    ("s", "term", "User Flow: Layar Laporan", "A", 0),
    ("n1", "io", "Tampil pendapatan bulan ini" + small("membership, visit harian, produk, total"), "A", 100),
    ("d1", "dec", "Ganti periode?", "A", 210),
    ("n2", "io", "Pilih hari / bulan / tahun ini atau tanggal sendiri", "B", 230),
    ("d2", "dec", "Rentang tanggal valid?", "B", 340),
    ("n3", "proc", "Hitung ulang pendapatan", "C", 360),
    ("e1", "io", "Tampil pesan error", "B", 490),
    ("t", "term", "Selesai", "A", 610),
], [
    ("s", "n1"), ("n1", "d1"),
    ("d1", "n2", {"label": "Ya", "out": "r", "inn": "l"}),
    ("d1", "t", {"label": "Tidak", "out": "b", "inn": "t"}),
    ("n2", "d2"),
    ("d2", "n3", {"label": "Ya", "out": "r", "inn": "l"}),
    ("n3", "n1", {"out": "t", "inn": "r", "dashed": True, "pts": [(700, 128)]}),
    ("d2", "e1", {"label": "Tidak", "out": "b", "inn": "t"}),
    ("e1", "t", {"out": "b", "inn": "r", "pts": [(430, 635)]}),
])

flow("userflow_7_akun", [
    ("s", "term", "User Flow: Layar Akun", "A", 0),
    ("d1", "dec", "User adalah SuperAdmin?", "A", 100),
    ("n2", "io", "User memilih bagian", "B", 120),
    ("a2", "proc", "Kelola admin", "C", 120),
    ("a1", "proc", "Akun saya", "B", 230),
    ("n1", "io", "Isi password lama &amp; baru", "A", 230),
    ("n3", "io", "Isi data admin baru, reset password, atau nonaktifkan admin", "C", 230),
    ("d2", "dec", "Data valid?", "A", 370),
    ("e1", "io", "Tampil pesan error", "B", 390),
    ("n4", "proc", "Simpan perubahan", "A", 520),
    ("t", "term", "Selesai", "A", 630),
], [
    ("s", "d1"),
    ("d1", "n1", {"label": "Tidak", "out": "b", "inn": "t"}),
    ("d1", "n2", {"label": "Ya", "out": "r", "inn": "l"}),
    ("n2", "a1", {"out": "b", "inn": "t"}),
    ("n2", "a2", {"out": "r", "inn": "l"}),
    ("a1", "n1", {"out": "l", "inn": "r"}),
    ("a2", "n3", {"out": "b", "inn": "t"}),
    ("n1", "d2"),
    ("n3", "d2", {"out": "b", "inn": "t", "pts": [(700, 335), (160, 335)]}),
    ("d2", "e1", {"label": "Tidak", "out": "r", "inn": "l"}),
    ("d2", "n4", {"label": "Ya", "out": "b", "inn": "t"}),
    ("n4", "t"),
    ("e1", "t", {"out": "b", "inn": "r", "pts": [(430, 655)]}),
])

# ---------- class diagram ----------
C_HEAD = "swimlane;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=30;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=0;marginBottom=0;" + BW + "fontSize=14;html=1;"
C_TXT = "text;strokeColor=none;fillColor=none;align=left;verticalAlign=top;spacingLeft=6;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;fontColor=#000000;fontSize=12;html=1;whiteSpace=wrap;"
C_LINE = "line;strokeWidth=1;fillColor=none;strokeColor=#000000;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;html=1;"
INH = S_EDGE + "endArrow=block;endFill=0;endSize=12;"
REAL = INH + "dashed=1;"
ASC = S_EDGE + "endArrow=open;endSize=10;"
COMP = S_EDGE + "startArrow=diamondThin;startFill=1;startSize=14;endArrow=none;"

d = Diagram()
d.v("title", "Class Diagram IndiGym (Model)", "text;html=1;fontSize=20;fontStyle=1;fontColor=#000000;align=left;verticalAlign=middle;", 40, 0, 500, 40)


def cls(id, name, x, y, w, attrs, methods, abstract=False):
    ha = 18 * len(attrs) + 8 if attrs else 0
    hm = 18 * len(methods) + 8
    d.v(id, name, C_HEAD + ("fontStyle=3;" if abstract else "fontStyle=1;"), x, y, w, 38 + ha + hm)
    yy = 30
    if attrs:
        d.v(id + "_a", "<br>".join(attrs), C_TXT, 0, yy, w, ha, parent=id)
        yy += ha
    d.v(id + "_l", "", C_LINE, 0, yy, w, 8, parent=id)
    d.v(id + "_m", "<br>".join(methods), C_TXT, 0, yy + 8, w, hm, parent=id)


I = lambda s: f"<i>{s}</i>"
cls("manusia", "Manusia", 420, 60, 240, ["- id: Long", "- nama: String"], [I("+ tampilkanInfo(): String"), "+ getNama(): String"], True)
cls("paket", "PaketAkses", 1040, 60, 340, ["- id: Long", "- nama: String", "- harga: BigDecimal", "- diarsipkan: boolean"],
    [I("+ hitungTanggalAkhir(mulai: LocalDate): LocalDate"), I("+ getJenis(): String"), I("+ getDurasiBulan(): Integer"), "+ getDurasiTeks(): String"], True)
cls("admin", "Admin", 40, 340, 260, ["- username: String", "- passwordHash: String", "- aktif: boolean"],
    ["+ bolehKelolaAdmin(): boolean", "+ getRole(): String", "+ getStatus(): String", "+ tampilkanInfo(): String"])
cls("plg", "Pelanggan", 440, 340, 260, ["- kode: String", "- kontak: String", "- diarsipkan: boolean"], ["+ getStatus(): String", "+ tampilkanInfo(): String"])
cls("pm", "PaketMembership", 840, 340, 320, ["- durasiBulan: int"], ["+ hitungTanggalAkhir(mulai: LocalDate): LocalDate", "+ getJenis(): String", "+ getDurasiBulan(): Integer"])
cls("ph", "PaketHarian", 1200, 340, 320, [], ["+ hitungTanggalAkhir(mulai: LocalDate): LocalDate", "+ getJenis(): String", "+ getDurasiBulan(): Integer"])
cls("sa", "SuperAdmin", 40, 620, 260, [], ["+ bolehKelolaAdmin(): boolean", "+ getRole(): String"])
cls("ci", "CheckIn", 340, 620, 280, ["- id: Long", "- pelanggan: Pelanggan", "- adminId: Long", "- pembayaranId: Long", "- waktu: LocalDateTime", "- hasil: String", "- alasan: String"],
    ["+ diizinkan(): boolean", "+ getKeterangan(): String"])
cls("pa", "PembayaranAkses", 700, 620, 300, ["- id: Long", "- pelanggan: Pelanggan", "- paketId: Long", "- adminId: Long", "- namaPaket: String", "- harga: BigDecimal",
                                            "- waktuBayar: LocalDateTime", "- tanggalMulai: LocalDate", "- tanggalAkhir: LocalDate"],
    ["+ berlakuPada(tanggal: LocalDate): boolean", "+ statusPada(hariIni: LocalDate): String", "+ getPeriode(): String", "+ cetakBukti(): String"])
cls("trx", "Transaksi", 1080, 620, 320, ["- id: Long", "- pelanggan: Pelanggan", "- adminId: Long", "- waktu: LocalDateTime", "- status: String", "- alasanBatal: String",
                                         "- daftarDetail: List&lt;DetailTransaksi&gt;"],
    ["+ hitungTotal(): BigDecimal", "+ sudahDibatalkan(): boolean", "+ cetakBukti(): String"])
cls("rp", "Rupiah", 40, 1000, 280, [], ["<u>+ format(nilai: BigDecimal): String</u>"])
cls("cetak", "«interface» Cetak", 800, 1000, 220, [], [I("+ cetakBukti(): String")])
cls("dt", "DetailTransaksi", 1100, 1000, 280, ["- produkId: Long", "- namaProduk: String", "- hargaSatuan: BigDecimal", "- jumlah: int"], ["+ hitungSubtotal(): BigDecimal"])
cls("prd", "Produk", 1500, 1000, 260, ["- id: Long", "- kode: String", "- nama: String", "- harga: BigDecimal", "- stok: int", "- diarsipkan: boolean"], ["+ setStok(stok: int): void"])
# titik keluar/masuk dan belokan diatur manual supaya garis tidak menembus Class lain
d.e("admin", "manusia", style=INH, out="t", inn=(0.3, 1), pts=[(170, 300), (492, 300)])
d.e("plg", "manusia", style=INH, out="t", inn=(0.625, 1))
d.e("sa", "admin", style=INH, out="t", inn="b")
d.e("pm", "paket", style=INH, out="t", inn=(0.35, 1), pts=[(1000, 300), (1159, 300)])
d.e("ph", "paket", style=INH, out="t", inn=(0.85, 1), pts=[(1360, 300), (1329, 300)])
d.e("pa", "cetak", style=REAL, out=(0.5, 1), inn=(0.3, 0), pts=[(850, 960), (866, 960)])
d.e("trx", "cetak", style=REAL, out=(0.2, 1), inn=(0.75, 0), pts=[(1144, 950), (965, 950)])
d.e("ci", "plg", style=ASC, out=(0.59, 0), inn=(0.25, 1))
d.e("pa", "plg", style=ASC, out="t", inn=(0.75, 1), pts=[(850, 560), (635, 560)])
d.e("trx", "plg", style=ASC, out=(0.3, 0), inn="r", pts=[(1176, 520), (780, 520), (780, 412)])
d.e("pa", "paket", style=ASC, out=(0.8, 0), inn="r", pts=[(940, 500), (1560, 500), (1560, 160)])
d.e("ci", "pa", style=ASC, out="r", inn="l")
d.e("dt", "prd", style=ASC, out="r", inn="l")
d.e("trx", "dt", style=COMP, out="b", inn="t")
d.v("legend", "<b>Keterangan</b><br>Garis + segitiga kosong = pewarisan (extends)<br>Garis putus + segitiga kosong = implementasi interface<br>"
              "Belah ketupat hitam = komposisi<br>Panah terbuka = asosiasi<br><i>Miring</i> = abstract, <u>garis bawah</u> = static",
    "text;html=1;align=left;verticalAlign=top;whiteSpace=wrap;fontSize=12;fontColor=#000000;strokeColor=#000000;fillColor=#FFFFFF;spacing=8;", 40, 1100, 400, 120)
d.save("class_diagram")

# ---------- use case ----------
d = Diagram()
UC = "ellipse;whiteSpace=wrap;html=1;" + BW + "fontSize=13;"
ACT = "shape=umlActor;verticalLabelPosition=bottom;verticalAlign=top;html=1;" + BW + "fontSize=14;"
LINE = "endArrow=none;html=1;strokeColor=#000000;exitX=1;exitY=0.5;entryX=0;entryY=0.5;"
DASH = "dashed=1;endArrow=open;endSize=10;html=1;strokeColor=#000000;fontColor=#000000;labelBackgroundColor=#FFFFFF;"
d.v("title", "Use Case Diagram IndiGym", "text;html=1;fontSize=20;fontStyle=1;fontColor=#000000;align=left;verticalAlign=middle;", 40, 0, 500, 40)
d.v("sys", "Sistem IndiGym", "swimlane;startSize=36;" + BW + "fontSize=16;html=1;", 220, 60, 540, 1060)
ucs = ["Login", "Logout", "Lihat Dashboard", "Kelola Pelanggan" + small("daftar, cari, ubah, arsip"), "Check-in Pelanggan", "Jual Visit Harian",
       "Bayar Membership", "Kelola Paket Membership" + small("tambah, arsipkan"), "Transaksi Penjualan", "Batalkan Transaksi",
       "Kelola Produk &amp; Stok", "Lihat Laporan Pendapatan", "Ganti Password Sendiri", "Kelola Akun Admin" + small("tambah, reset password, nonaktifkan")]
for i, u in enumerate(ucs):
    d.v(f"u{i}", u, UC, 40, 40 + 72 * i, 210, 56, parent="sys")
d.v("x1", "Izinkan Kunjungan Kedua", UC, 310, 328, 200, 56, parent="sys")
d.v("x2", "Cetak Bukti / Struk", UC, 310, 508, 200, 56, parent="sys")
d.e("x1", "u4", "«extend»", style=DASH, parent="sys")
d.e("u6", "x2", "«include»", style=DASH, parent="sys")
d.e("u5", "x2", style=DASH, parent="sys")
d.e("u8", "x2", style=DASH, parent="sys")
d.v("admin", "Admin", ACT, 80, 510, 30, 60)
d.v("sadmin", "SuperAdmin", ACT, 80, 1034, 30, 60)
d.e("sadmin", "admin", style="endArrow=block;endFill=0;endSize=12;html=1;strokeColor=#000000;")
for i in range(13):
    d.e("admin", f"u{i}", style=LINE)
d.e("sadmin", "u13", style=LINE)
d.v("note", "SuperAdmin mewarisi semua use case Admin, ditambah Kelola Akun Admin.", "text;html=1;align=left;verticalAlign=top;whiteSpace=wrap;fontSize=12;fontColor=#000000;", 220, 1130, 540, 30)
d.save("use_case")
print("ok")
