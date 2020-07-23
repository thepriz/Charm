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
        'parrots_stay_on_shoulder': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraft.entity.player.PlayerEntity',
                'methodName': 'func_192030_dh', // spawnShoulderEntities
                'methodDesc': '()V'
            },
            transformer: function(method) {
                var success = false;
                var newInstructions = new InsnList();
                var arrayLength = method.instructions.size();
                var j = 0;

                for (var i = 0; i < arrayLength; ++i) {
                    var instruction = method.instructions.get(i);
                    if (instruction.getOpcode() == Opcodes.IFGE) {
                        var label = new LabelNode();
                        newInstructions.add(ASM.buildMethodCall(ASM_HOOKS, "stayOnShoulder", "()Z", ASM.MethodType.STATIC));
                        newInstructions.add(new JumpInsnNode(Opcodes.IFEQ, label));
                        newInstructions.add(new InsnNode(Opcodes.RETURN));
                        newInstructions.add(label);

                        method.instructions.insert(instruction, newInstructions);
                        success = true;
                        break;
                    }
                }

                print("[Charm ASM] (ParrotsStayOnShoulder) " + (success ? "Patched PlayerEntity" : "Failed to patch PlayerEntity"));
                return method;
            }
        }
    }
}