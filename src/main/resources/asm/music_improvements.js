function initializeCoreMod() {
    var ASM_HOOKS = "svenhjol/charm/base/CharmAsmHooks";
    var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
    var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
    var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
    var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
    var IincInsnNode = Java.type('org.objectweb.asm.tree.IincInsnNode');
    var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
    var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
    var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
    var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');

    return {
        'music_improvements_tick': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraft.client.audio.MusicTicker',
                'methodName': 'func_73660_a', // tick
                'methodDesc': '()V'
            },
            transformer: function(method) {
                var success = false;
                var newInstructions = new InsnList();
                var instruction = method.instructions.get(0);

                var label = new LabelNode();
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                newInstructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/audio/MusicTicker", ASM.mapField("field_147678_c"), "Lnet/minecraft/client/audio/ISound;")); // currentMusic
                newInstructions.add(ASM.buildMethodCall(ASM_HOOKS, "handleMusicTick", "(Lnet/minecraft/client/audio/ISound;)Z", ASM.MethodType.STATIC))
                newInstructions.add(new JumpInsnNode(Opcodes.IFEQ, label));
                newInstructions.add(new InsnNode(Opcodes.RETURN));
                newInstructions.add(label);

                method.instructions.insertBefore(instruction, newInstructions);
                print("[Charm ASM] (MusicImprovements) Patched MusicTicker (tick)");
                return method;
            }
        },
        'music_improvements_stop': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraft.client.audio.MusicTicker',
                'methodName': 'func_209200_a', // stop
                'methodDesc': '()V'
            },
            transformer: function(method) {
                var success = false;
                var newInstructions = new InsnList();
                var instruction = method.instructions.get(0);

                var label = new LabelNode();
                newInstructions.add(ASM.buildMethodCall(ASM_HOOKS, "handleMusicStop", "()Z", ASM.MethodType.STATIC));
                newInstructions.add(new JumpInsnNode(Opcodes.IFEQ, label));
                newInstructions.add(new InsnNode(Opcodes.RETURN));
                newInstructions.add(label);

                method.instructions.insertBefore(instruction, newInstructions);
                print("[Charm ASM] (MusicImprovements) Patched MusicTicker (stop)");
                return method;
            }
        },
        'music_improvements_play': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraft.client.audio.MusicTicker',
                'methodName': 'func_239540_b_', // isPlaying
                'methodDesc': '(Lnet/minecraft/client/audio/BackgroundMusicSelector;)Z'
            },
            transformer: function(method) {
                var success = false;
                var newInstructions = new InsnList();
                var instruction = method.instructions.get(0);

                var label = new LabelNode();
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                newInstructions.add(ASM.buildMethodCall(ASM_HOOKS, "handleMusicPlaying", "(Lnet/minecraft/client/audio/BackgroundMusicSelector;)Z", ASM.MethodType.STATIC));
                newInstructions.add(new JumpInsnNode(Opcodes.IFEQ, label));
                newInstructions.add(new InsnNode(Opcodes.ICONST_1));
                newInstructions.add(new InsnNode(Opcodes.IRETURN));
                newInstructions.add(label);

                method.instructions.insertBefore(instruction, newInstructions);
                print("[Charm ASM] (MusicImprovements) Patched MusicTicker (play)");
                return method;
            }

        }
    }
}