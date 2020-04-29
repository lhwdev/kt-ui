package com.asmx.ui.plugin.compiler;

import java.lang.System;

@kotlin.Metadata(mv = {1, 4, 0}, bv = {1, 0, 3}, k = 1, xi = 2, d1 = {"\u0000\u0096\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0001\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\b\u0002\u0018\u00002\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0006\u0012\u0004\u0018\u00010\u00030\u0001:\u0002\u00cd\u0001B\u000f\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J&\u0010\u0014\u001a\u00020\u00022\u001b\u0010\u0015\u001a\u0017\u0012\b\u0012\u00060\u0017j\u0002`\u0018\u0012\u0004\u0012\u00020\u00190\u0016\u00a2\u0006\u0002\b\u001aH\u0082\bJ\u000e\u0010\u001b\u001a\u00020\u00022\u0006\u0010\u001c\u001a\u00020\u001dJ%\u0010\u001e\u001a\u00020\u00022\u0016\u0010\u001f\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010\u00020 \"\u0004\u0018\u00010\u0002H\u0002\u00a2\u0006\u0002\u0010!J\u000e\u0010\"\u001a\u00020\u00022\u0006\u0010#\u001a\u00020$J\u0016\u0010%\u001a\u00020\u00022\f\u0010&\u001a\b\u0012\u0004\u0012\u00020\u001d0\'H\u0002J\u001a\u0010(\u001a\u00020\u00022\u0006\u0010)\u001a\u00020*2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010,\u001a\u00020\u00022\u0006\u0010-\u001a\u00020.2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010/\u001a\u00020\u00022\u0006\u00100\u001a\u0002012\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u00102\u001a\u00020\u00022\u0006\u00103\u001a\u0002042\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u00105\u001a\u00020\u00022\u0006\u00106\u001a\u0002072\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u00108\u001a\u00020\u00022\u0006\u0010-\u001a\u0002092\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010:\u001a\u00020\u00022\u0006\u0010;\u001a\u00020<2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010=\u001a\u00020\u00022\u0006\u0010)\u001a\u00020>2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010?\u001a\u00020\u00022\u0006\u0010-\u001a\u00020@2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010A\u001a\u00020\u00022\u0006\u0010-\u001a\u00020B2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J&\u0010C\u001a\u00020\u0002\"\u0004\b\u0000\u0010D2\f\u0010-\u001a\b\u0012\u0004\u0012\u0002HD0E2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010F\u001a\u00020\u00022\u0006\u0010)\u001a\u00020G2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010H\u001a\u00020\u00022\u0006\u0010-\u001a\u00020\u001d2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010I\u001a\u00020\u00022\u0006\u00106\u001a\u00020J2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010K\u001a\u00020\u00022\u0006\u0010)\u001a\u00020L2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010M\u001a\u00020\u00022\u0006\u0010-\u001a\u00020N2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010O\u001a\u00020\u00022\u0006\u0010P\u001a\u00020Q2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010R\u001a\u00020\u00022\u0006\u0010-\u001a\u00020S2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010T\u001a\u00020\u00022\u0006\u0010-\u001a\u00020U2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010V\u001a\u00020\u00022\u0006\u0010W\u001a\u00020X2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010Y\u001a\u00020\u00022\u0006\u0010-\u001a\u00020Z2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010[\u001a\u00020\u00022\u0006\u0010)\u001a\u00020\\2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010]\u001a\u00020\u00022\u0006\u0010-\u001a\u00020^2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010_\u001a\u00020\u00022\u0006\u0010)\u001a\u00020`2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010a\u001a\u00020\u00022\u0006\u0010-\u001a\u00020b2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010c\u001a\u00020\u00022\u0006\u0010-\u001a\u00020d2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010e\u001a\u00020\u00022\u0006\u00100\u001a\u00020f2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010g\u001a\u00020\u00022\u0006\u0010)\u001a\u00020h2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010i\u001a\u00020\u00022\u0006\u0010)\u001a\u00020j2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010k\u001a\u00020\u00022\u0006\u0010)\u001a\u00020l2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010m\u001a\u00020\u00022\u0006\u0010)\u001a\u00020n2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010o\u001a\u00020\u00022\u0006\u0010-\u001a\u00020p2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010q\u001a\u00020\u00022\u0006\u0010-\u001a\u00020r2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010s\u001a\u00020\u00022\u0006\u0010-\u001a\u00020t2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010u\u001a\u00020\u00022\u0006\u0010-\u001a\u00020v2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010w\u001a\u00020\u00022\u0006\u0010-\u001a\u00020x2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010y\u001a\u00020\u00022\u0006\u0010-\u001a\u00020z2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010{\u001a\u00020\u00022\u0006\u0010-\u001a\u00020|2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010}\u001a\u00020\u00022\u0006\u0010-\u001a\u00020~2\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001b\u0010\u007f\u001a\u00020\u00022\u0007\u0010)\u001a\u00030\u0080\u00012\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001c\u0010\u0081\u0001\u001a\u00020\u00022\u0007\u0010-\u001a\u00030\u0082\u00012\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001c\u0010\u0083\u0001\u001a\u00020\u00022\u0007\u0010)\u001a\u00030\u0084\u00012\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001c\u0010\u0085\u0001\u001a\u00020\u00022\u0007\u0010)\u001a\u00030\u0086\u00012\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001c\u0010\u0087\u0001\u001a\u00020\u00022\u0007\u0010-\u001a\u00030\u0088\u00012\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001c\u0010\u0089\u0001\u001a\u00020\u00022\u0007\u0010-\u001a\u00030\u008a\u00012\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001c\u0010\u008b\u0001\u001a\u00020\u00022\u0007\u0010)\u001a\u00030\u008c\u00012\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001c\u0010\u008d\u0001\u001a\u00020\u00022\u0007\u0010-\u001a\u00030\u008e\u00012\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001c\u0010\u008f\u0001\u001a\u00020\u00022\u0007\u0010-\u001a\u00030\u0090\u00012\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001c\u0010\u0091\u0001\u001a\u00020\u00022\u0007\u0010)\u001a\u00030\u0092\u00012\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001d\u0010\u0093\u0001\u001a\u00020\u00022\b\u0010\u0094\u0001\u001a\u00030\u0095\u00012\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001c\u0010\u0096\u0001\u001a\u00020\u00022\u0007\u0010-\u001a\u00030\u0097\u00012\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001c\u0010\u0098\u0001\u001a\u00020\u00022\u0007\u00100\u001a\u00030\u0099\u00012\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001c\u0010\u009a\u0001\u001a\u00020\u00022\u0007\u0010-\u001a\u00030\u009b\u00012\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001d\u0010\u009c\u0001\u001a\u00020\u00022\b\u0010\u009d\u0001\u001a\u00030\u009e\u00012\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001c\u0010\u009f\u0001\u001a\u00020\u00022\u0007\u0010)\u001a\u00030\u00a0\u00012\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001c\u0010\u00a1\u0001\u001a\u00020\u00022\u0007\u0010-\u001a\u00030\u00a2\u00012\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001c\u0010\u00a3\u0001\u001a\u00020\u00022\u0007\u0010)\u001a\u00030\u00a4\u00012\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001c\u0010\u00a5\u0001\u001a\u00020\u00022\u0007\u0010)\u001a\u00030\u00a6\u00012\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001c\u0010\u00a7\u0001\u001a\u00020\u00022\u0007\u0010-\u001a\u00030\u00a8\u00012\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001b\u0010\u00a9\u0001\u001a\u00020\u00022\u0006\u0010)\u001a\u00020\u00112\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001c\u0010\u00aa\u0001\u001a\u00020\u00022\u0007\u0010-\u001a\u00030\u00ab\u00012\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016J\u001c\u0010\u00ac\u0001\u001a\u00020\u00022\u0007\u0010P\u001a\u00030\u00ad\u00012\b\u0010+\u001a\u0004\u0018\u00010\u0003H\u0016JL\u0010\u00ae\u0001\u001a\u00020\u0019\"\t\b\u0000\u0010D*\u00030\u00af\u0001*\u00060\u0017j\u0002`\u00182\u0007\u0010\u00b0\u0001\u001a\u00020\u00022\t\u0010\u00b1\u0001\u001a\u0004\u0018\u0001HD2\u0013\u0010\u00b2\u0001\u001a\u000e\u0012\u0004\u0012\u0002HD\u0012\u0004\u0012\u00020\u00020\u0016H\u0082\b\u00a2\u0006\u0003\u0010\u00b3\u0001J\u000f\u0010\u00b4\u0001\u001a\u00030\u00af\u0001*\u00030\u00af\u0001H\u0002J\u000e\u0010\u00b5\u0001\u001a\u00020\u0002*\u00030\u00b6\u0001H\u0002J\u0018\u0010\u001b\u001a\u00020\u0019*\u00060\u0017j\u0002`\u00182\u0006\u0010\u001c\u001a\u00020\u001dH\u0002J\u001c\u0010\u00b7\u0001\u001a\u00020\u0019*\u00060\u0017j\u0002`\u00182\t\u0010\u00b8\u0001\u001a\u0004\u0018\u00010XH\u0002J\r\u0010\u00b9\u0001\u001a\u00020\u0002*\u00020>H\u0002J\r\u0010\u00ba\u0001\u001a\u00020\u0002*\u00020GH\u0002J\r\u0010\u00bb\u0001\u001a\u00020\u0002*\u00020jH\u0002J\u000e\u0010\u00bc\u0001\u001a\u00020\u0002*\u00030\u0080\u0001H\u0002J\u000e\u0010\u00bd\u0001\u001a\u00020\u0002*\u00030\u0086\u0001H\u0002J\r\u0010\u00be\u0001\u001a\u00020\u0002*\u00020$H\u0002J\u000e\u0010\u00bf\u0001\u001a\u00020\u0002*\u00030\u0092\u0001H\u0002J\r\u0010\u00c0\u0001\u001a\u00020\u0002*\u000209H\u0002J\u000e\u0010\u00c1\u0001\u001a\u00020\u0002*\u00030\u00c2\u0001H\u0002J\u000e\u0010\u00c3\u0001\u001a\u00020\u0002*\u00030\u00a0\u0001H\u0002J\u000e\u0010\u00c4\u0001\u001a\u00020\u0002*\u00030\u00c5\u0001H\u0002J\u000e\u0010\u00c6\u0001\u001a\u00020\u0002*\u00030\u00b6\u0001H\u0002J\r\u0010\u00c7\u0001\u001a\u00020\u0002*\u00020nH\u0002J\u000e\u0010\u00c8\u0001\u001a\u00020\u0002*\u00030\u00a6\u0001H\u0002J\r\u0010\u00c9\u0001\u001a\u00020\u0002*\u00020nH\u0002J\r\u0010\u00ca\u0001\u001a\u00020\u0002*\u00020\u0011H\u0002J3\u0010\u00cb\u0001\u001a\u00020\u0002\"\u0004\b\u0000\u0010D*\u0002HD2\u0017\u0010\u0015\u001a\u0013\u0012\u0004\u0012\u0002HD\u0012\u0004\u0012\u00020\u00020\u0016\u00a2\u0006\u0002\b\u001aH\u0082\b\u00a2\u0006\u0003\u0010\u00cc\u0001R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0012\u0010\f\u001a\u00060\rR\u00020\u0000X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0018\u0010\u0010\u001a\u00020\u0002*\u00020\u00118BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013\u00a8\u0006\u00ce\u0001"}, d2 = {"Lcom/asmx/ui/plugin/compiler/RenderIrElementVisitor;", "Lorg/jetbrains/kotlin/ir/visitors/IrElementVisitor;", "", "", "normalizeNames", "", "(Z)V", "descriptorRendererForErrorDeclarations", "Lorg/jetbrains/kotlin/renderer/DescriptorRenderer;", "nameMap", "", "Lorg/jetbrains/kotlin/ir/symbols/IrVariableSymbol;", "symbolReferenceRenderer", "Lcom/asmx/ui/plugin/compiler/RenderIrElementVisitor$BoundSymbolReferenceRenderer;", "temporaryIndex", "", "normalizedName", "Lorg/jetbrains/kotlin/ir/declarations/IrVariable;", "getNormalizedName", "(Lorg/jetbrains/kotlin/ir/declarations/IrVariable;)Ljava/lang/String;", "buildTrimEnd", "fn", "Lkotlin/Function1;", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "", "Lkotlin/ExtensionFunctionType;", "renderAsAnnotation", "irAnnotation", "Lorg/jetbrains/kotlin/ir/expressions/IrConstructorCall;", "renderFlagsList", "flags", "", "([Ljava/lang/String;)Ljava/lang/String;", "renderSymbolReference", "symbol", "Lorg/jetbrains/kotlin/ir/symbols/IrSymbol;", "renderTypeAnnotations", "annotations", "", "visitAnonymousInitializer", "declaration", "Lorg/jetbrains/kotlin/ir/declarations/IrAnonymousInitializer;", "data", "visitBlock", "expression", "Lorg/jetbrains/kotlin/ir/expressions/IrBlock;", "visitBlockBody", "body", "Lorg/jetbrains/kotlin/ir/expressions/IrBlockBody;", "visitBranch", "branch", "Lorg/jetbrains/kotlin/ir/expressions/IrBranch;", "visitBreak", "jump", "Lorg/jetbrains/kotlin/ir/expressions/IrBreak;", "visitCall", "Lorg/jetbrains/kotlin/ir/expressions/IrCall;", "visitCatch", "aCatch", "Lorg/jetbrains/kotlin/ir/expressions/IrCatch;", "visitClass", "Lorg/jetbrains/kotlin/ir/declarations/IrClass;", "visitClassReference", "Lorg/jetbrains/kotlin/ir/expressions/IrClassReference;", "visitComposite", "Lorg/jetbrains/kotlin/ir/expressions/IrComposite;", "visitConst", "T", "Lorg/jetbrains/kotlin/ir/expressions/IrConst;", "visitConstructor", "Lorg/jetbrains/kotlin/ir/declarations/IrConstructor;", "visitConstructorCall", "visitContinue", "Lorg/jetbrains/kotlin/ir/expressions/IrContinue;", "visitDeclaration", "Lorg/jetbrains/kotlin/ir/declarations/IrDeclaration;", "visitDelegatingConstructorCall", "Lorg/jetbrains/kotlin/ir/expressions/IrDelegatingConstructorCall;", "visitDoWhileLoop", "loop", "Lorg/jetbrains/kotlin/ir/expressions/IrDoWhileLoop;", "visitDynamicMemberExpression", "Lorg/jetbrains/kotlin/ir/expressions/IrDynamicMemberExpression;", "visitDynamicOperatorExpression", "Lorg/jetbrains/kotlin/ir/expressions/IrDynamicOperatorExpression;", "visitElement", "element", "Lorg/jetbrains/kotlin/ir/IrElement;", "visitEnumConstructorCall", "Lorg/jetbrains/kotlin/ir/expressions/IrEnumConstructorCall;", "visitEnumEntry", "Lorg/jetbrains/kotlin/ir/declarations/IrEnumEntry;", "visitErrorCallExpression", "Lorg/jetbrains/kotlin/ir/expressions/IrErrorCallExpression;", "visitErrorDeclaration", "Lorg/jetbrains/kotlin/ir/declarations/IrErrorDeclaration;", "visitErrorExpression", "Lorg/jetbrains/kotlin/ir/expressions/IrErrorExpression;", "visitExpression", "Lorg/jetbrains/kotlin/ir/expressions/IrExpression;", "visitExpressionBody", "Lorg/jetbrains/kotlin/ir/expressions/IrExpressionBody;", "visitExternalPackageFragment", "Lorg/jetbrains/kotlin/ir/declarations/IrExternalPackageFragment;", "visitField", "Lorg/jetbrains/kotlin/ir/declarations/IrField;", "visitFile", "Lorg/jetbrains/kotlin/ir/declarations/IrFile;", "visitFunction", "Lorg/jetbrains/kotlin/ir/declarations/IrFunction;", "visitFunctionExpression", "Lorg/jetbrains/kotlin/ir/expressions/IrFunctionExpression;", "visitFunctionReference", "Lorg/jetbrains/kotlin/ir/expressions/IrFunctionReference;", "visitGetClass", "Lorg/jetbrains/kotlin/ir/expressions/IrGetClass;", "visitGetEnumValue", "Lorg/jetbrains/kotlin/ir/expressions/IrGetEnumValue;", "visitGetField", "Lorg/jetbrains/kotlin/ir/expressions/IrGetField;", "visitGetObjectValue", "Lorg/jetbrains/kotlin/ir/expressions/IrGetObjectValue;", "visitGetValue", "Lorg/jetbrains/kotlin/ir/expressions/IrGetValue;", "visitInstanceInitializerCall", "Lorg/jetbrains/kotlin/ir/expressions/IrInstanceInitializerCall;", "visitLocalDelegatedProperty", "Lorg/jetbrains/kotlin/ir/declarations/IrLocalDelegatedProperty;", "visitLocalDelegatedPropertyReference", "Lorg/jetbrains/kotlin/ir/expressions/IrLocalDelegatedPropertyReference;", "visitModuleFragment", "Lorg/jetbrains/kotlin/ir/declarations/IrModuleFragment;", "visitProperty", "Lorg/jetbrains/kotlin/ir/declarations/IrProperty;", "visitPropertyReference", "Lorg/jetbrains/kotlin/ir/expressions/IrPropertyReference;", "visitReturn", "Lorg/jetbrains/kotlin/ir/expressions/IrReturn;", "visitScript", "Lorg/jetbrains/kotlin/ir/declarations/IrScript;", "visitSetField", "Lorg/jetbrains/kotlin/ir/expressions/IrSetField;", "visitSetVariable", "Lorg/jetbrains/kotlin/ir/expressions/IrSetVariable;", "visitSimpleFunction", "Lorg/jetbrains/kotlin/ir/declarations/IrSimpleFunction;", "visitSpreadElement", "spread", "Lorg/jetbrains/kotlin/ir/expressions/IrSpreadElement;", "visitStringConcatenation", "Lorg/jetbrains/kotlin/ir/expressions/IrStringConcatenation;", "visitSyntheticBody", "Lorg/jetbrains/kotlin/ir/expressions/IrSyntheticBody;", "visitThrow", "Lorg/jetbrains/kotlin/ir/expressions/IrThrow;", "visitTry", "aTry", "Lorg/jetbrains/kotlin/ir/expressions/IrTry;", "visitTypeAlias", "Lorg/jetbrains/kotlin/ir/declarations/IrTypeAlias;", "visitTypeOperator", "Lorg/jetbrains/kotlin/ir/expressions/IrTypeOperatorCall;", "visitTypeParameter", "Lorg/jetbrains/kotlin/ir/declarations/IrTypeParameter;", "visitValueParameter", "Lorg/jetbrains/kotlin/ir/declarations/IrValueParameter;", "visitVararg", "Lorg/jetbrains/kotlin/ir/expressions/IrVararg;", "visitVariable", "visitWhen", "Lorg/jetbrains/kotlin/ir/expressions/IrWhen;", "visitWhileLoop", "Lorg/jetbrains/kotlin/ir/expressions/IrWhileLoop;", "appendNullableAttribute", "", "prefix", "value", "toString", "(Ljava/lang/StringBuilder;Ljava/lang/String;Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)V", "escapeIfRequired", "render", "Lorg/jetbrains/kotlin/ir/types/IrType;", "renderAsAnnotationArgument", "irElement", "renderClassFlags", "renderConstructorFlags", "renderFieldFlags", "renderLocalDelegatedPropertyFlags", "renderPropertyFlags", "renderReference", "renderSimpleFunctionFlags", "renderSuperQualifier", "renderTypeAbbreviation", "Lorg/jetbrains/kotlin/ir/types/IrTypeAbbreviation;", "renderTypeAliasFlags", "renderTypeArgument", "Lorg/jetbrains/kotlin/ir/types/IrTypeArgument;", "renderTypeInner", "renderTypeParameters", "renderValueParameterFlags", "renderValueParameterTypes", "renderVariableFlags", "runTrimEnd", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)Ljava/lang/String;", "BoundSymbolReferenceRenderer", "compiler-plugin"})
final class RenderIrElementVisitor implements org.jetbrains.kotlin.ir.visitors.IrElementVisitor {
    private final java.util.Map<org.jetbrains.kotlin.ir.symbols.IrVariableSymbol, java.lang.String> nameMap = null;
    private int temporaryIndex = 0;
    private final com.asmx.ui.plugin.compiler.RenderIrElementVisitor.BoundSymbolReferenceRenderer symbolReferenceRenderer = null;
    private final org.jetbrains.kotlin.renderer.DescriptorRenderer descriptorRendererForErrorDeclarations = null;
    private final boolean normalizeNames = false;
    
    private final java.lang.String getNormalizedName(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrVariable $this$normalizedName) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String renderSymbolReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrSymbol symbol) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final java.lang.String renderAsAnnotation(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrConstructorCall irAnnotation) {
        return null;
    }
    
    private final void renderAsAnnotation(@org.jetbrains.annotations.NotNull
    java.lang.StringBuilder $this$renderAsAnnotation, org.jetbrains.kotlin.ir.expressions.IrConstructorCall irAnnotation) {
    }
    
    private final void renderAsAnnotationArgument(@org.jetbrains.annotations.NotNull
    java.lang.StringBuilder $this$renderAsAnnotationArgument, org.jetbrains.kotlin.ir.IrElement irElement) {
    }
    
    private final java.lang.String buildTrimEnd(kotlin.jvm.functions.Function1<? super java.lang.StringBuilder, kotlin.Unit> fn) {
        return null;
    }
    
    private final <T extends java.lang.Object>java.lang.String runTrimEnd(T $this$runTrimEnd, kotlin.jvm.functions.Function1<? super T, java.lang.String> fn) {
        return null;
    }
    
    private final java.lang.String render(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.types.IrType $this$render) {
        return null;
    }
    
    private final java.lang.String renderTypeInner(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.types.IrType $this$renderTypeInner) {
        return null;
    }
    
    private final java.lang.String renderTypeAbbreviation(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.types.IrTypeAbbreviation $this$renderTypeAbbreviation) {
        return null;
    }
    
    private final java.lang.String renderTypeArgument(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.types.IrTypeArgument $this$renderTypeArgument) {
        return null;
    }
    
    private final java.lang.String renderTypeAnnotations(java.util.List<? extends org.jetbrains.kotlin.ir.expressions.IrConstructorCall> annotations) {
        return null;
    }
    
    private final java.lang.String renderReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.symbols.IrSymbol $this$renderReference) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitElement(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.IrElement element, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitDeclaration(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrDeclaration declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitModuleFragment(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrModuleFragment declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitExternalPackageFragment(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrExternalPackageFragment declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitFile(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrFile declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitFunction(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrFunction declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitScript(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrScript declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitSimpleFunction(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrSimpleFunction declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    private final java.lang.String renderFlagsList(java.lang.String... flags) {
        return null;
    }
    
    private final java.lang.String renderSimpleFunctionFlags(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrSimpleFunction $this$renderSimpleFunctionFlags) {
        return null;
    }
    
    private final java.lang.String renderTypeParameters(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrFunction $this$renderTypeParameters) {
        return null;
    }
    
    private final java.lang.String renderValueParameterTypes(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrFunction $this$renderValueParameterTypes) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitConstructor(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrConstructor declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    private final java.lang.String renderConstructorFlags(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrConstructor $this$renderConstructorFlags) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitProperty(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrProperty declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    private final java.lang.String renderPropertyFlags(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrProperty $this$renderPropertyFlags) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitField(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrField declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    private final java.lang.String renderFieldFlags(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrField $this$renderFieldFlags) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitClass(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrClass declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    private final java.lang.String renderClassFlags(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrClass $this$renderClassFlags) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitVariable(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrVariable declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    private final java.lang.String renderVariableFlags(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrVariable $this$renderVariableFlags) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitEnumEntry(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrEnumEntry declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitAnonymousInitializer(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrAnonymousInitializer declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitTypeParameter(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrTypeParameter declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitValueParameter(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrValueParameter declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    private final java.lang.String renderValueParameterFlags(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrValueParameter $this$renderValueParameterFlags) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitLocalDelegatedProperty(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrLocalDelegatedProperty declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitTypeAlias(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrTypeAlias declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    private final java.lang.String renderTypeAliasFlags(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrTypeAlias $this$renderTypeAliasFlags) {
        return null;
    }
    
    private final java.lang.String renderLocalDelegatedPropertyFlags(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrLocalDelegatedProperty $this$renderLocalDelegatedPropertyFlags) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitExpressionBody(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrExpressionBody body, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitBlockBody(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrBlockBody body, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitSyntheticBody(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrSyntheticBody body, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrExpression expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public <T extends java.lang.Object>java.lang.String visitConst(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrConst<T> expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    private final java.lang.Object escapeIfRequired(@org.jetbrains.annotations.NotNull
    java.lang.Object $this$escapeIfRequired) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitVararg(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrVararg expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitSpreadElement(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrSpreadElement spread, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitBlock(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrBlock expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitComposite(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrComposite expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitReturn(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrReturn expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrCall expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    private final java.lang.String renderSuperQualifier(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrCall $this$renderSuperQualifier) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitConstructorCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrConstructorCall expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitDelegatingConstructorCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrDelegatingConstructorCall expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitEnumConstructorCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrEnumConstructorCall expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitInstanceInitializerCall(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrInstanceInitializerCall expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitGetValue(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetValue expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitSetVariable(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrSetVariable expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitGetField(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetField expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitSetField(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrSetField expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitGetObjectValue(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetObjectValue expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitGetEnumValue(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetEnumValue expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitStringConcatenation(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrStringConcatenation expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitTypeOperator(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrTypeOperatorCall expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitWhen(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrWhen expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitBranch(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrBranch branch, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitWhileLoop(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrWhileLoop loop, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitDoWhileLoop(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrDoWhileLoop loop, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitBreak(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrBreak jump, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitContinue(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrContinue jump, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitThrow(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrThrow expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitFunctionReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrFunctionReference expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitPropertyReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrPropertyReference expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    private final <T extends java.lang.Object>void appendNullableAttribute(@org.jetbrains.annotations.NotNull
    java.lang.StringBuilder $this$appendNullableAttribute, java.lang.String prefix, T value, kotlin.jvm.functions.Function1<? super T, java.lang.String> toString) {
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitLocalDelegatedPropertyReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrLocalDelegatedPropertyReference expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitFunctionExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrFunctionExpression expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitClassReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrClassReference expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitGetClass(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetClass expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitTry(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrTry aTry, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitCatch(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrCatch aCatch, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitDynamicOperatorExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrDynamicOperatorExpression expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitDynamicMemberExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrDynamicMemberExpression expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitErrorDeclaration(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrErrorDeclaration declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitErrorExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrErrorExpression expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    @java.lang.Override
    public java.lang.String visitErrorCallExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrErrorCallExpression expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    public RenderIrElementVisitor(boolean normalizeNames) {
        super();
    }
    
    public RenderIrElementVisitor() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public java.lang.String visitBody(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrBody body, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public java.lang.String visitBreakContinue(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrBreakContinue jump, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public java.lang.String visitCallableReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrCallableReference expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public java.lang.String visitContainerExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrContainerExpression expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public java.lang.String visitDeclarationReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrDeclarationReference expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public java.lang.String visitDynamicExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrDynamicExpression expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public java.lang.String visitElseBranch(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrElseBranch branch, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public java.lang.String visitFieldAccess(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrFieldAccessExpression expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public java.lang.String visitFunctionAccess(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrFunctionAccessExpression expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public java.lang.String visitLoop(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrLoop loop, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public java.lang.String visitMemberAccess(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrMemberAccessExpression expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public java.lang.String visitPackageFragment(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.declarations.IrPackageFragment declaration, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public java.lang.String visitSingletonReference(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrGetSingletonValue expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public java.lang.String visitSuspendableExpression(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrSuspendableExpression expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public java.lang.String visitSuspensionPoint(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrSuspensionPoint expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public java.lang.String visitValueAccess(@org.jetbrains.annotations.NotNull
    org.jetbrains.kotlin.ir.expressions.IrValueAccessExpression expression, @org.jetbrains.annotations.Nullable
    java.lang.Void data) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 4, 0}, bv = {1, 0, 3}, k = 1, xi = 2, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0010\u0001\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0082\u0004\u0018\u00002\u0010\u0012\u0004\u0012\u00020\u0002\u0012\u0006\u0012\u0004\u0018\u00010\u00030\u0001B\u0005\u00a2\u0006\u0002\u0010\u0004J\u001a\u0010\u0005\u001a\u00020\u00022\u0006\u0010\u0006\u001a\u00020\u00072\b\u0010\b\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010\t\u001a\u00020\u00022\u0006\u0010\n\u001a\u00020\u000b2\b\u0010\b\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010\f\u001a\u00020\u00022\u0006\u0010\n\u001a\u00020\r2\b\u0010\b\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010\u000e\u001a\u00020\u00022\u0006\u0010\n\u001a\u00020\u000f2\b\u0010\b\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010\u0010\u001a\u00020\u00022\u0006\u0010\n\u001a\u00020\u00112\b\u0010\b\u001a\u0004\u0018\u00010\u0003H\u0016J\u001a\u0010\u0012\u001a\u00020\u00022\u0006\u0010\n\u001a\u00020\u00132\b\u0010\b\u001a\u0004\u0018\u00010\u0003H\u0016J\u0018\u0010\u0014\u001a\u00020\u0015*\u00060\u0016j\u0002`\u00172\u0006\u0010\u0018\u001a\u00020\u0019H\u0002J\u0018\u0010\u001a\u001a\u00020\u0015*\u00060\u0016j\u0002`\u00172\u0006\u0010\u0006\u001a\u00020\u001bH\u0002J\u0018\u0010\u001c\u001a\u00020\u0015*\u00060\u0016j\u0002`\u00172\u0006\u0010\n\u001a\u00020\u0019H\u0002J\u0018\u0010\u001d\u001a\u00020\u0015*\u00060\u0016j\u0002`\u00172\u0006\u0010\n\u001a\u00020\u001eH\u0002\u00a8\u0006\u001f"}, d2 = {"Lcom/asmx/ui/plugin/compiler/RenderIrElementVisitor$BoundSymbolReferenceRenderer;", "Lorg/jetbrains/kotlin/ir/visitors/IrElementVisitor;", "", "", "(Lcom/asmx/ui/plugin/compiler/RenderIrElementVisitor;)V", "visitElement", "element", "Lorg/jetbrains/kotlin/ir/IrElement;", "data", "visitFunction", "declaration", "Lorg/jetbrains/kotlin/ir/declarations/IrFunction;", "visitLocalDelegatedProperty", "Lorg/jetbrains/kotlin/ir/declarations/IrLocalDelegatedProperty;", "visitProperty", "Lorg/jetbrains/kotlin/ir/declarations/IrProperty;", "visitValueParameter", "Lorg/jetbrains/kotlin/ir/declarations/IrValueParameter;", "visitVariable", "Lorg/jetbrains/kotlin/ir/declarations/IrVariable;", "renderDeclaredIn", "", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "irDeclaration", "Lorg/jetbrains/kotlin/ir/declarations/IrDeclaration;", "renderElementNameFallback", "", "renderParentOfReferencedDeclaration", "renderTypeParameters", "Lorg/jetbrains/kotlin/ir/declarations/IrTypeParametersContainer;", "compiler-plugin"})
    final class BoundSymbolReferenceRenderer implements org.jetbrains.kotlin.ir.visitors.IrElementVisitor {
        
        @org.jetbrains.annotations.NotNull
        @java.lang.Override
        public java.lang.String visitElement(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.IrElement element, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        @java.lang.Override
        public java.lang.String visitVariable(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.declarations.IrVariable declaration, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        @java.lang.Override
        public java.lang.String visitValueParameter(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.declarations.IrValueParameter declaration, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        @java.lang.Override
        public java.lang.String visitFunction(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.declarations.IrFunction declaration, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        private final void renderTypeParameters(@org.jetbrains.annotations.NotNull
        java.lang.StringBuilder $this$renderTypeParameters, org.jetbrains.kotlin.ir.declarations.IrTypeParametersContainer declaration) {
        }
        
        @org.jetbrains.annotations.NotNull
        @java.lang.Override
        public java.lang.String visitProperty(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.declarations.IrProperty declaration, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        @java.lang.Override
        public java.lang.String visitLocalDelegatedProperty(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.declarations.IrLocalDelegatedProperty declaration, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        private final void renderDeclaredIn(@org.jetbrains.annotations.NotNull
        java.lang.StringBuilder $this$renderDeclaredIn, org.jetbrains.kotlin.ir.declarations.IrDeclaration irDeclaration) {
        }
        
        private final void renderParentOfReferencedDeclaration(@org.jetbrains.annotations.NotNull
        java.lang.StringBuilder $this$renderParentOfReferencedDeclaration, org.jetbrains.kotlin.ir.declarations.IrDeclaration declaration) {
        }
        
        private final void renderElementNameFallback(@org.jetbrains.annotations.NotNull
        java.lang.StringBuilder $this$renderElementNameFallback, java.lang.Object element) {
        }
        
        public BoundSymbolReferenceRenderer() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitAnonymousInitializer(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.declarations.IrAnonymousInitializer declaration, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitBlock(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrBlock expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitBlockBody(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrBlockBody body, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitBody(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrBody body, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitBranch(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrBranch branch, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitBreak(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrBreak jump, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitBreakContinue(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrBreakContinue jump, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitCall(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrCall expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitCallableReference(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrCallableReference expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitCatch(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrCatch aCatch, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitClass(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.declarations.IrClass declaration, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitClassReference(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrClassReference expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitComposite(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrComposite expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public <T extends java.lang.Object>java.lang.String visitConst(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrConst<T> expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitConstructor(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.declarations.IrConstructor declaration, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitConstructorCall(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrConstructorCall expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitContainerExpression(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrContainerExpression expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitContinue(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrContinue jump, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitDeclaration(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.declarations.IrDeclaration declaration, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitDeclarationReference(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrDeclarationReference expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitDelegatingConstructorCall(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrDelegatingConstructorCall expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitDoWhileLoop(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrDoWhileLoop loop, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitDynamicExpression(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrDynamicExpression expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitDynamicMemberExpression(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrDynamicMemberExpression expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitDynamicOperatorExpression(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrDynamicOperatorExpression expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitElseBranch(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrElseBranch branch, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitEnumConstructorCall(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrEnumConstructorCall expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitEnumEntry(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.declarations.IrEnumEntry declaration, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitErrorCallExpression(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrErrorCallExpression expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitErrorDeclaration(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.declarations.IrErrorDeclaration declaration, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitErrorExpression(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrErrorExpression expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitExpression(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrExpression expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitExpressionBody(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrExpressionBody body, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitExternalPackageFragment(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.declarations.IrExternalPackageFragment declaration, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitField(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.declarations.IrField declaration, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitFieldAccess(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrFieldAccessExpression expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitFile(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.declarations.IrFile declaration, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitFunctionAccess(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrFunctionAccessExpression expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitFunctionExpression(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrFunctionExpression expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitFunctionReference(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrFunctionReference expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitGetClass(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrGetClass expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitGetEnumValue(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrGetEnumValue expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitGetField(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrGetField expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitGetObjectValue(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrGetObjectValue expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitGetValue(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrGetValue expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitInstanceInitializerCall(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrInstanceInitializerCall expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitLocalDelegatedPropertyReference(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrLocalDelegatedPropertyReference expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitLoop(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrLoop loop, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitMemberAccess(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrMemberAccessExpression expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitModuleFragment(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.declarations.IrModuleFragment declaration, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitPackageFragment(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.declarations.IrPackageFragment declaration, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitPropertyReference(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrPropertyReference expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitReturn(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrReturn expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitScript(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.declarations.IrScript declaration, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitSetField(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrSetField expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitSetVariable(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrSetVariable expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitSimpleFunction(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.declarations.IrSimpleFunction declaration, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitSingletonReference(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrGetSingletonValue expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitSpreadElement(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrSpreadElement spread, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitStringConcatenation(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrStringConcatenation expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitSuspendableExpression(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrSuspendableExpression expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitSuspensionPoint(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrSuspensionPoint expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitSyntheticBody(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrSyntheticBody body, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitThrow(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrThrow expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitTry(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrTry aTry, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitTypeAlias(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.declarations.IrTypeAlias declaration, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitTypeOperator(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrTypeOperatorCall expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitTypeParameter(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.declarations.IrTypeParameter declaration, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitValueAccess(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrValueAccessExpression expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitVararg(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrVararg expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitWhen(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrWhen expression, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public java.lang.String visitWhileLoop(@org.jetbrains.annotations.NotNull
        org.jetbrains.kotlin.ir.expressions.IrWhileLoop loop, @org.jetbrains.annotations.Nullable
        java.lang.Void data) {
            return null;
        }
    }
}